package com.calmwrist.wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.calmwrist.wear.model.EpisodeRecord
import com.calmwrist.wear.model.StressBand
import com.calmwrist.wear.model.Trend
import com.calmwrist.wear.model.computeEpisodeStats
import com.calmwrist.wear.theme.CalmWristColors

/**
 * Solo los números que ya guardábamos y nunca mostrábamos: nada de IA ni
 * red acá. En una pantalla redonda de 1.2" un dashboard con varios
 * gráficos no entra legible — así que en vez de eso van 2-3 datos clave
 * y una mini-visualización por franja horaria, no un panel de analítica.
 */
@Composable
fun HistoryScreen(episodes: List<EpisodeRecord>, onClose: () -> Unit) {
    val accent = CalmWristColors.accentFor(StressBand.CALM)
    val stats = computeEpisodeStats(episodes)

    Column(
        modifier = Modifier
            .fillMaxSize()
