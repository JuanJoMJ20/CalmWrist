package com.calmwrist.wear.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

private val calmWristPalette = Colors(
    primary = CalmWristColors.accentFor(com.calmwrist.wear.model.StressBand.CALM),
    background = CalmWristColors.background,
    surface = CalmWristColors.surface,
    onPrimary = CalmWristColors.background,
    onBackground = CalmWristColors.textPrimary,
    onSurface = CalmWristColors.textPrimary,
    error = CalmWristColors.danger,
)

@Composable
fun CalmWristTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = calmWristPalette, content = content)
}
