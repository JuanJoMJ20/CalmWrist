package com.calmwrist.wear.llm

import com.calmwrist.wear.model.EpisodeRecord
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Escribe la carta mensual: un resumen reflexivo y cálido de los
 * episodios manejados, no un reporte clínico. Solo se le manda a la IA
 * información ya agregada (cuántos episodios, a qué hora, cuánto
 * duraron) — nunca pulso crudo ni ningún otro dato biométrico.
 */
object GeminiReflectionService {
    suspend fun generateLetter(episodes: List<EpisodeRecord>): String {
        if (episodes.isEmpty()) {
            return "Todavía no registraste ningún episodio. Cuando pase el primero, " +
                "voy a poder contarte algo sobre tu propio patrón."
        }
        return GeminiClient.generateText(
            prompt = buildPrompt(episodes),
            maxOutputTokens = 220,
            jsonResponse = false,
        )
    }

    private fun buildPrompt(episodes: List<EpisodeRecord>): String {
        val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale("es"))

        val summary = episodes.joinToString("\n") { episode ->
            val date = java.util.Date(episode.timestampMillis)
            val minutes = TimeUnit.SECONDS.toMinutes(episode.durationSeconds)
            val seconds = episode.durationSeconds % 60
            "- ${dayFormat.format(date)} a las ${hourFormat.format(date)}, " +
                "tardó ${minutes}m ${seconds}s en calmarse"
        }

        return """
            Eres la voz de CalmWrist, un reloj que acompaña a alguien con
            ansiedad. Tu personalidad es cálida, directa y segura — nunca
