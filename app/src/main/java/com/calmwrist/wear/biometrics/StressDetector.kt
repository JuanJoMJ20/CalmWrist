package com.calmwrist.wear.biometrics

import com.calmwrist.wear.model.StressBand

object StressDetector {
    const val ELEVATED_BPM_DELTA = 20.0
    const val CRISIS_BPM_DELTA = 35.0

    fun bandFor(bpmDelta: Double): StressBand = when {
        bpmDelta >= CRISIS_BPM_DELTA -> StressBand.CRISIS
        bpmDelta >= ELEVATED_BPM_DELTA -> StressBand.ELEVATED
        else -> StressBand.CALM
    }
}
