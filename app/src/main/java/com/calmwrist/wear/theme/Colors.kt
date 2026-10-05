package com.calmwrist.wear.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.calmwrist.wear.model.StressBand

/**
 * Fondo casi negro en todas las bandas: maximiza contraste y es legible
 * con luz solar directa o visión periférica durante una crisis.
 * El color de acento es lo único que se mueve entre bandas, y nunca usa
 * rojo puro: un rojo de alarma puede retroalimentar el pánico en vez de
 * calmarlo, así que la "urgencia" se comunica con un ámbar cálido.
 */
object CalmWristColors {
    val background = Color(0xFF050607)
    val surface = Color(0xFF14181C)
    val surfaceGlow = Color(0xFF1B2124)
    val textPrimary = Color(0xFFF5F7F8)
    val textSecondary = Color(0xFFA7B0B8)
    val danger = Color(0xFFFF6B57)

    private val accent = mapOf(
        StressBand.CALM to Color(0xFF63E6CB),
        StressBand.ELEVATED to Color(0xFFFFD166),
        StressBand.CRISIS to Color(0xFFFFB020),
    )

    /** Tinte muy tenue del acento, usado solo para el resplandor de fondo. */
    private val glowTint = mapOf(
        StressBand.CALM to Color(0xFF123832),
