package com.calmwrist.wear.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.calmwrist.wear.model.StressBand
import com.calmwrist.wear.theme.CalmWristColors

private enum class Phase(val label: String, val durationMs: Int, val hapticPattern: LongArray) {
    INHALE("Inhala", 4000, longArrayOf(0, 120)),
    HOLD("Sostén", 7000, longArrayOf(0, 60, 90, 60)),
    EXHALE("Exhala", 8000, longArrayOf(0, 200)),
}

private fun nextPhase(current: Phase): Phase = when (current) {
    Phase.INHALE -> Phase.HOLD
    Phase.HOLD -> Phase.EXHALE
    Phase.EXHALE -> Phase.INHALE
}

private const val CIRCLE_SIZE_DP = 104
private const val GLOW_SIZE_DP = 146

/**
 * Técnica 4-7-8 (inhalar 4s / sostener 7s / exhalar 8s): el patrón más
 * citado en manejo clínico de ansiedad aguda, porque la exhalación larga
 * activa el sistema parasimpático. El patrón háptico distingue cada fase
 * por NÚMERO de golpes, pensado para que se note incluso sin mirar.
 */
@Composable
fun HapticBreathingLoop(
    active: Boolean,
    band: StressBand,
    onCycleComplete: () -> Unit = {},
) {
    val context = LocalContext.current
    val scale = remember { Animatable(0.55f) }
    var phase by remember { mutableStateOf(Phase.INHALE) }

    LaunchedEffect(active) {
        if (!active) return@LaunchedEffect
        while (true) {
            vibrate(context, phase.hapticPattern)
            scale.animateTo(
                targetValue = if (phase == Phase.EXHALE) 0.55f else 1f,
                animationSpec = tween(durationMillis = phase.durationMs),
            )
            val next = nextPhase(phase)
            phase = next
            if (next == Phase.INHALE) onCycleComplete()
        }
    }

    val accent by animateColorAsState(
        targetValue = CalmWristColors.accentFor(band),
        animationSpec = tween(durationMillis = 900),
        label = "breathing-accent",
    )

    Box(
        modifier = Modifier
            .size(GLOW_SIZE_DP.dp)
            .semantics {
                liveRegion = LiveRegionMode.Assertive
                contentDescription = "${phase.label}. Sigue el ritmo de la vibración."
            },
        contentAlignment = Alignment.Center,
    ) {
        // Resplandor: un degradado radial detrás del aro principal que se
        // expande y contrae con la misma respiración, como si el círculo
        // emitiera luz propia en vez de ser solo un trazo plano.
        Box(
            modifier = Modifier
                .size(GLOW_SIZE_DP.dp)
                .scale(scale.value)
