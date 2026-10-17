package com.calmwrist.wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.calmwrist.wear.model.HumanizedErrorContent
import com.calmwrist.wear.theme.CalmWristColors

/**
 * Las 3 partes son props obligatorias (HumanizedErrorContent): ningún
 * error del sistema puede renderizarse sin decir qué pasó, por qué, y
 * qué hacer ahora. Nunca mostrar código de error ni stack trace aquí.
 */
@Composable
fun HumanizedErrorScreen(
    content: HumanizedErrorContent,
    actionLabel: String = "Entendido",
    onAction: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CalmWristColors.background)
            .padding(top = 8.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
            .semantics { liveRegion = LiveRegionMode.Assertive },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(CalmWristColors.danger.copy(alpha = 0.16f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "!",
                color = CalmWristColors.danger,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
