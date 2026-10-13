package com.calmwrist.wear.llm

import com.calmwrist.wear.model.PersonalCapsule
import org.json.JSONObject

/**
 * Genera la cápsula de contención personalizada UNA sola vez, en el
 * onboarding — nunca durante una crisis real. La intervención en vivo
 * siempre lee el texto ya guardado en [com.calmwrist.wear.data.CapsuleStore],
 * para no depender de red justo cuando más importa responder rápido.
 */
object GeminiCapsuleService {
