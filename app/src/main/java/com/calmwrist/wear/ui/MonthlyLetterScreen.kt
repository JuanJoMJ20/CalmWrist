package com.calmwrist.wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.calmwrist.wear.model.StressBand
import com.calmwrist.wear.theme.CalmWristColors

/**
 * Carta reflexiva, no un dashboard: la lectura es lineal, sin números
 * resaltados ni gráficos — el formato acompaña el tono cálido del texto
 * que genera la IA, en vez de contradecirlo con una estética clínica.
 */
@Composable
fun MonthlyLetterScreen(
    isLoading: Boolean,
    letterText: String?,
    onClose: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CalmWristColors.background)
            .padding(top = 18.dp, bottom = 18.dp, start = 20.dp, end = 20.dp)
            // La carta la escribe la IA: su largo varía, y en la pantalla
            // redonda y chica del reloj un texto de más de 2-3 oraciones
            // se corta sin esta línea, sin ninguna forma de bajar a leer
            // el resto.
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(indicatorColor = CalmWristColors.accentFor(StressBand.CALM))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Pensando en tu mes…",
                color = CalmWristColors.textSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
            return@Column
        }

        Text(
            text = "Tu carta",
            color = CalmWristColors.accentFor(StressBand.CALM),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = letterText ?: "No pude escribir tu carta ahora. Prueba de nuevo más tarde.",
            color = CalmWristColors.textPrimary,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Chip(
            onClick = onClose,
            shape = RoundedCornerShape(50),
            colors = ChipDefaults.chipColors(
                backgroundColor = CalmWristColors.surface,
                contentColor = CalmWristColors.textPrimary,
            ),
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
