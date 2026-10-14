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
    suspend fun generateCapsule(answers: List<String>): PersonalCapsule {
        val text = GeminiClient.generateText(
            prompt = buildPrompt(answers),
            maxOutputTokens = 200,
            jsonResponse = true,
        )
        val json = JSONObject(text)
        return PersonalCapsule(
            title = json.getString("title").trim(),
            subtitle = json.getString("subtitle").trim(),
        )
    }

    private fun buildPrompt(answers: List<String>): String {
        val place = answers.getOrElse(0) { "" }
        val person = answers.getOrElse(1) { "" }
        val phrase = answers.getOrElse(2) { "" }

        return """
            Eres la voz de CalmWrist, un reloj que acompaña a alguien durante
            un ataque de pánico. Tu personalidad es cálida, directa y segura
            — nunca clínica, nunca robótica. Usa siempre "tú" (nunca "vos").

            Esta persona contó, en sus propias palabras:
            - Lugar que le da paz: "$place"
            - Persona o recuerdo que la calma: "$person"
