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
