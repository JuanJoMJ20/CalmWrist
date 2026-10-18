package com.calmwrist.wear.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.calmwrist.wear.copy.postCrisisAffirmation
import com.calmwrist.wear.copy.toneCopyFor
import com.calmwrist.wear.model.PersonalCapsule
import com.calmwrist.wear.model.StressBand
import com.calmwrist.wear.theme.CalmWristColors

/**
 * Pantalla única, sin navegación: en una crisis no hay carga cognitiva
 * disponible para recordar dónde está un botón en una jerarquía de menús.
 * En CRISIS el ejercicio arranca solo (intervención proactiva); en
 * CALM/ELEVATED el usuario lo activa con el único botón disponible.
 */
@Composable
fun InterventionScreen(
    band: StressBand,
    onResolved: () -> Unit,
    capsule: PersonalCapsule? = null,
    justResolvedAffirmation: Boolean = false,
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    var breathingActive by remember { mutableStateOf(false) }
    val copy = if (band == StressBand.CALM && justResolvedAffirmation) {
        postCrisisAffirmation
    } else {
        toneCopyFor(band, capsule)
    }
    val isCrisis = band == StressBand.CRISIS
    val showBreathing = breathingActive || isCrisis

    // El acento y el resplandor de fondo transicionan suavemente en vez de
    // saltar de golpe: el cambio de tono debe sentirse como una marea, no
    // como un sobresalto — sobre todo si la banda sube hacia crisis.
    val accent by animateColorAsState(
        targetValue = CalmWristColors.accentFor(band),
        animationSpec = tween(durationMillis = 900),
        label = "accent",
    )

    Column(
        modifier = modifier
            .background(CalmWristColors.backgroundGlowFor(band))
            .padding(top = 30.dp, bottom = 34.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        AnimatedContent(
            targetState = copy.title,
            transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(200)) },
            label = "title",
        ) { title ->
            Text(
                text = title,
                color = CalmWristColors.textPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
