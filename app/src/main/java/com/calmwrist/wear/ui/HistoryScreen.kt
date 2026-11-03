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
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.foundation.rotary.rotaryScrollable
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
    val scrollState = rememberScrollState()
    val focusRequester = rememberActiveFocusRequester()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CalmWristColors.background)
            .padding(top = 26.dp, bottom = 26.dp, start = 24.dp, end = 24.dp)
            .verticalScroll(scrollState)
            // Permite bajar girando la corona/bisel, no solo con el dedo:
            // el texto de historial puede ser más largo que la pantalla.
            .rotaryScrollable(
                behavior = RotaryScrollableDefaults.behavior(scrollState),
                focusRequester = focusRequester,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Tu historial",
            color = accent,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (stats == null) {
            Text(
                text = "Todavía no hay episodios registrados.",
                color = CalmWristColors.textSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
        } else {
            Text(
                text = "${stats.count}",
                color = CalmWristColors.textPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
            )
            Text(
                text = if (stats.count == 1) "episodio registrado" else "episodios registrados",
                color = CalmWristColors.textSecondary,
                fontSize = 11.sp,
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Duración promedio",
                color = CalmWristColors.textSecondary,
                fontSize = 10.sp,
            )
            Text(
                text = "${stats.avgDurationSeconds}s",
                color = CalmWristColors.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(14.dp))

            val (trendText, trendColor) = when (stats.trend) {
                Trend.IMPROVING -> "↓ Tus episodios se están acortando" to accent
                Trend.WORSENING -> "↑ Tus episodios se están alargando" to CalmWristColors.danger
                Trend.STABLE -> "→ Se mantiene estable" to CalmWristColors.textSecondary
                Trend.NOT_ENOUGH_DATA -> "Necesitamos más episodios para ver una tendencia" to CalmWristColors.textSecondary
            }
            Text(
                text = trendText,
                color = trendColor,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Cuándo sueles pasarla peor",
                color = CalmWristColors.textSecondary,
                fontSize = 10.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            val maxCount = (stats.buckets.maxOfOrNull { it.count } ?: 0).coerceAtLeast(1)
            stats.buckets.forEach { bucket ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = bucket.label,
                        color = CalmWristColors.textSecondary,
                        fontSize = 10.sp,
                        modifier = Modifier.width(66.dp),
                    )
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .padding(end = 24.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(bucket.count.toFloat() / maxCount)
                                .background(accent.copy(alpha = if (bucket.count == 0) 0.15f else 0.8f), RoundedCornerShape(50)),
                        )
                    }
                    Text(
                        text = "${bucket.count}",
                        color = CalmWristColors.textSecondary,
                        fontSize = 10.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        Chip(
            onClick = onClose,
            shape = RoundedCornerShape(50),
            colors = ChipDefaults.chipColors(backgroundColor = CalmWristColors.surface, contentColor = CalmWristColors.textPrimary),
            label = {
                Text(
                    text = "Cerrar",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
    }
}
