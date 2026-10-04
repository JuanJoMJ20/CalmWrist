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

/**
 * La Voz de CalmWrist es siempre la misma (identidad estable y segura).
 * El Tono es la variable que cambia con el contexto emocional del usuario.
 */
enum class ToneMode {
    EMPATHETIC,
    IMPERATIVE_CALMING,
}

data class ToneCopy(
    val tone: ToneMode,
    val title: String,
    val subtitle: String,
    val actionLabel: String,
)

/**
 * Las 3 partes son obligatorias a nivel de tipo: ningún error del sistema
 * puede construirse sin decir qué pasó, por qué, y qué hacer ahora.
 */
data class HumanizedErrorContent(
    val whatHappened: String,
    val why: String,
    val whatToDoNow: String,
)
