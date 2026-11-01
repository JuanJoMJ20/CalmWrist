package com.calmwrist.wear

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.calmwrist.wear.copy.ErrorPresets
import com.calmwrist.wear.model.StressBand
import com.calmwrist.wear.theme.CalmWristColors
import com.calmwrist.wear.theme.CalmWristTheme
import com.calmwrist.wear.ui.HistoryScreen
import com.calmwrist.wear.ui.HumanizedErrorScreen
import com.calmwrist.wear.ui.InterventionScreen
import com.calmwrist.wear.ui.MonthlyLetterScreen
import com.calmwrist.wear.ui.OnboardingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalmWristTheme {
                CalmWristApp()
            }
        }
    }
}

@Composable
private fun CalmWristApp(viewModel: MainViewModel = viewModel()) {
    var permissionDenied by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            permissionDenied = false
            viewModel.start()
        } else {
            permissionDenied = true
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.BODY_SENSORS)
    }

    // En crisis no hay interacción táctil esperable (la persona está
    // respirando, no tocando el reloj): sin esto, el timeout de pantalla
    // de Wear OS apaga la pantalla o te devuelve a la carita del reloj
    // justo en el peor momento. Se libera apenas la banda deja de ser
    // CRISIS, para no gastar batería el resto del tiempo.
    val band by viewModel.band.collectAsState()
    val activity = LocalContext.current as? Activity
    DisposableEffect(band, activity) {
        val window = activity?.window
        if (band == StressBand.CRISIS) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    val error by viewModel.error.collectAsState()
    val capsule by viewModel.capsule.collectAsState()
    val onboardingPending by viewModel.onboardingPending.collectAsState()
    val isGeneratingCapsule by viewModel.isGeneratingCapsule.collectAsState()
    val capsuleGenerationFailed by viewModel.capsuleGenerationFailed.collectAsState()
    val hasEpisodeHistory by viewModel.hasEpisodeHistory.collectAsState()
    val showMonthlyLetter by viewModel.showMonthlyLetter.collectAsState()
    val isGeneratingLetter by viewModel.isGeneratingLetter.collectAsState()
    val letterText by viewModel.letterText.collectAsState()
    val showHistory by viewModel.showHistory.collectAsState()
    val episodes by viewModel.episodes.collectAsState()
    val justResolvedAffirmation by viewModel.justResolvedAffirmation.collectAsState()
    val currentError = error

    // El pulso real no baja al instante apenas se resuelve una crisis — el
    // sensor sigue mandando lecturas y el cuerpo tarda en asentarse, así
    // que sin esto la banda real vuelve a ELEVATED casi al toque y el
    // mensaje de "ya estás bien" nunca llega a mostrarse. Una crisis real
    // nueva sigue ganando siempre: justResolvedAffirmation se apaga apenas
    // el pulso vuelve a CRISIS (ver MainViewModel.setBand).
    val effectiveBand = if (justResolvedAffirmation) StressBand.CALM else band

    when {
        permissionDenied -> HumanizedErrorScreen(
            content = ErrorPresets.sensorPermissionDenied,
            onAction = { permissionLauncher.launch(Manifest.permission.BODY_SENSORS) },
        )
        currentError != null -> HumanizedErrorScreen(
            content = currentError,
            onAction = { viewModel.dismissError() },
        )
        onboardingPending -> OnboardingScreen(
            isGenerating = isGeneratingCapsule,
            generationFailed = capsuleGenerationFailed,
            onComplete = { answers -> viewModel.generateCapsule(answers) },
            onSkip = { viewModel.dismissOnboarding() },
        )
        showMonthlyLetter -> MonthlyLetterScreen(
            isLoading = isGeneratingLetter,
            letterText = letterText,
            onClose = { viewModel.closeMonthlyLetter() },
        )
        showHistory -> HistoryScreen(
            episodes = episodes,
            onClose = { viewModel.closeHistory() },
        )
        else -> Box(modifier = Modifier.fillMaxSize()) {
            // La pantalla real siempre ocupa el 100% del espacio, igual
            // que en producción; los controles flotan encima sin reducir
            // el área disponible para el diseño real.
            InterventionScreen(
                band = effectiveBand,
                onResolved = { viewModel.resolveCrisis() },
                capsule = capsule,
                justResolvedAffirmation = justResolvedAffirmation,
                modifier = Modifier.fillMaxSize(),
            )
            if (hasEpisodeHistory && effectiveBand == StressBand.CALM) {
                // Padding generoso a propósito: pegado al borde de arriba
                // (como estaba antes, a 6dp) el cristal curvo de una
                // pantalla redonda corta el texto por los costados — acá
                // abajo, a la altura del título, ya hay ancho de sobra.
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 20.dp),
                ) {
                    Text(
                        text = "Carta",
                        color = CalmWristColors.accentFor(StressBand.CALM).copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        modifier = Modifier.clickable { viewModel.openMonthlyLetter() },
                    )
                    Text(
                        text = "  ·  ",
                        color = CalmWristColors.textSecondary.copy(alpha = 0.4f),
                        fontSize = 10.sp,
                    )
                    Text(
                        text = "Historial",
                        color = CalmWristColors.accentFor(StressBand.CALM).copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        modifier = Modifier.clickable { viewModel.openHistory() },
                    )
                }
            }
        }
    }
}
