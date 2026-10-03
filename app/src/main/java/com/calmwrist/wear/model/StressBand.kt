package com.calmwrist.wear.model

/**
 * Banda de estrés biométrico derivada de la frecuencia cardíaca.
 * - CALM: línea base, sin señales de alteración.
 * - ELEVATED: el pulso empieza a subir; aún no es una crisis.
 * - CRISIS: patrón compatible con inicio de ataque de pánico.
 */
enum class StressBand {
    CALM,
    ELEVATED,
    CRISIS,
}
