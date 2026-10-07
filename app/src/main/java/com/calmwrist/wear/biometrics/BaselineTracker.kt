package com.calmwrist.wear.biometrics

/**
 * Línea base personal de pulso en reposo, como media móvil exponencial.
 * Se congela mientras el usuario está en ELEVATED/CRISIS: si siguiera
 * actualizándose durante una crisis real, terminaría "normalizando" un
 * pulso alto como si fuera la línea base, y la detección dejaría de
 * disparar justo cuando más importa.
 */
class BaselineTracker(private val alpha: Double = 0.02) {
    private var baseline: Double? = null

