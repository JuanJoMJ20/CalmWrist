package com.calmwrist.wear

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.calmwrist.wear.biometrics.BaselineTracker
import com.calmwrist.wear.biometrics.BiometricStressService
import com.calmwrist.wear.biometrics.StressDetector
import com.calmwrist.wear.copy.ErrorPresets
import com.calmwrist.wear.data.AiDebugStore
import com.calmwrist.wear.data.CapsuleStore
import com.calmwrist.wear.data.EpisodeHistoryStore
import com.calmwrist.wear.debug.DebugBridge
import com.calmwrist.wear.llm.GeminiCapsuleService
import com.calmwrist.wear.llm.GeminiReflectionService
import com.calmwrist.wear.model.EpisodeRecord
import com.calmwrist.wear.model.HumanizedErrorContent
import com.calmwrist.wear.model.PersonalCapsule
import com.calmwrist.wear.model.StressBand
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val baseline = BaselineTracker()
    private val capsuleStore = CapsuleStore(application)
    private val episodeHistoryStore = EpisodeHistoryStore(application)
    private val aiDebugStore = AiDebugStore(application)

    init {
        // Permite que el panel de control de la demo (vía adb broadcast)
        // dispare acciones sobre esta instancia viva del ViewModel.
        DebugBridge.attach(this)
    }

    private val _band = MutableStateFlow(StressBand.CALM)
    val band: StateFlow<StressBand> = _band.asStateFlow()

    private val _error = MutableStateFlow<HumanizedErrorContent?>(null)
    val error: StateFlow<HumanizedErrorContent?> = _error.asStateFlow()

    private val _capsule = MutableStateFlow(capsuleStore.load())
    val capsule: StateFlow<PersonalCapsule?> = _capsule.asStateFlow()

    private val _onboardingPending = MutableStateFlow(capsuleStore.isOnboardingPending())
    val onboardingPending: StateFlow<Boolean> = _onboardingPending.asStateFlow()

    private val _isGeneratingCapsule = MutableStateFlow(false)
    val isGeneratingCapsule: StateFlow<Boolean> = _isGeneratingCapsule.asStateFlow()

    private val _capsuleGenerationFailed = MutableStateFlow(false)
    val capsuleGenerationFailed: StateFlow<Boolean> = _capsuleGenerationFailed.asStateFlow()

    private val _hasEpisodeHistory = MutableStateFlow(episodeHistoryStore.load().isNotEmpty())
    val hasEpisodeHistory: StateFlow<Boolean> = _hasEpisodeHistory.asStateFlow()

    private val _showMonthlyLetter = MutableStateFlow(false)
    val showMonthlyLetter: StateFlow<Boolean> = _showMonthlyLetter.asStateFlow()

    private val _showHistory = MutableStateFlow(false)
    val showHistory: StateFlow<Boolean> = _showHistory.asStateFlow()

    private val _justResolvedAffirmation = MutableStateFlow(false)
    val justResolvedAffirmation: StateFlow<Boolean> = _justResolvedAffirmation.asStateFlow()

    private val _episodes = MutableStateFlow(episodeHistoryStore.load())
    val episodes: StateFlow<List<EpisodeRecord>> = _episodes.asStateFlow()

    private val _isGeneratingLetter = MutableStateFlow(false)
    val isGeneratingLetter: StateFlow<Boolean> = _isGeneratingLetter.asStateFlow()

    private val _letterText = MutableStateFlow<String?>(null)
    val letterText: StateFlow<String?> = _letterText.asStateFlow()

    /** Anulación manual para probar en el emulador, que no siempre simula HR real. */
    private var debugOverrideActive = false
    private var crisisStartedAtMillis: Long? = null

    private var started = false

    fun start() {
        if (started) return
        started = true

        val service = BiometricStressService(getApplication())
        viewModelScope.launch {
            if (!service.hasHeartRateCapability()) {
                _error.value = ErrorPresets.sensorUnsupported
                return@launch
            }
            service.heartRateFlow().collect { reading ->
                if (debugOverrideActive) return@collect
                val isCalm = _band.value == StressBand.CALM
                val currentBaseline = baseline.update(reading.bpm, isCalm)
                val delta = reading.bpm - currentBaseline
                setBand(StressDetector.bandFor(delta))
            }
        }
    }

    private fun setBand(newBand: StressBand) {
        if (newBand == StressBand.CRISIS && _band.value != StressBand.CRISIS) {
            crisisStartedAtMillis = System.currentTimeMillis()
            _justResolvedAffirmation.value = false
        }
        _band.value = newBand
    }

    /**
     * Única llamada a la IA generativa de todo el flujo: corre en el
     * onboarding, no en una crisis real. Si falla (sin red, error de API),
     * se cae al texto genérico y la app sigue funcionando igual.
     */
    fun generateCapsule(answers: List<String>) {
        viewModelScope.launch {
            _isGeneratingCapsule.value = true
            _capsuleGenerationFailed.value = false
            try {
                val generated = GeminiCapsuleService.generateCapsule(answers)
                capsuleStore.save(generated)
                _capsule.value = generated
                _onboardingPending.value = false
                aiDebugStore.recordCapsule(answers, generated.title, generated.subtitle, error = null)
            } catch (e: Exception) {
                Log.e("CalmWrist", "Fallo al generar la cápsula con Gemini", e)
                _capsuleGenerationFailed.value = true
                aiDebugStore.recordCapsule(answers, title = null, subtitle = null, error = e.message ?: e.toString())
            } finally {
                _isGeneratingCapsule.value = false
            }
        }
    }

    fun dismissOnboarding() {
        capsuleStore.markOnboardingDismissed()
        _onboardingPending.value = false
        _capsuleGenerationFailed.value = false
    }

    fun setDebugBand(band: StressBand) {
        debugOverrideActive = true
        setBand(band)
    }

    fun dismissError() {
        _error.value = null
    }

    fun resolveCrisis() {
        val startedAt = crisisStartedAtMillis
        if (startedAt != null) {
            val durationSeconds = (System.currentTimeMillis() - startedAt) / 1000
            episodeHistoryStore.append(EpisodeRecord(timestampMillis = startedAt, durationSeconds = durationSeconds))
            _hasEpisodeHistory.value = true
            _episodes.value = episodeHistoryStore.load()
            crisisStartedAtMillis = null
        }
        _band.value = StressBand.CALM

        // Un refuerzo positivo fijo, visible unos segundos, antes de caer
        // al mensaje genérico de calma de siempre — ver postCrisisAffirmation.
