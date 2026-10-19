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
