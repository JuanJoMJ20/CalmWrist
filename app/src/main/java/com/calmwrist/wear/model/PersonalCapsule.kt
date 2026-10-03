package com.calmwrist.wear.model

/**
 * Mensaje de contención generado una sola vez (onboarding) a partir de
 * lo que la propia persona contó que la calma. Reemplaza el texto
 * genérico de CRISIS por algo escrito para ella. Se guarda local y
 * nunca se vuelve a generar en el momento de una crisis real.
 */
data class PersonalCapsule(
    val title: String,
    val subtitle: String,
)
