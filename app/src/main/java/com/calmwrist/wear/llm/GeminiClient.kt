package com.calmwrist.wear.llm

import com.calmwrist.wear.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class CapsuleGenerationException(message: String) : Exception(message)

/** Google dice explícitamente que un 503 es temporal: vale la pena reintentar. */
private class RetryableServerException(message: String) : Exception(message)

/**
 * Cliente mínimo y compartido para el nivel gratuito de Gemini
 * (aistudio.google.com). Centraliza la llamada HTTP, el reintento ante
 * sobrecarga del servidor (503) y la extracción del texto crudo de la
 * respuesta — cada caso de uso (cápsula, carta mensual) arma su propio
 * prompt y decide qué hacer con el texto devuelto.
 *
 * Si en el futuro [MODEL] deja de estar disponible, la propia API
 * devuelve en el error 404 cuál es el modelo vigente recomendado.
 *
 * Usamos la variante "lite" (no la "flash" completa): para un JSON corto
 * como este, el modelo completo activa razonamiento extendido interno y
 * tarda demasiado — confirmado directamente contra la API, no es un
 * problema de red ni del dispositivo. "lite" responde en segundos.
 */
object GeminiClient {
    private const val MODEL = "gemini-flash-lite-latest"
    private const val ENDPOINT =
        "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"

    init {
        // Refuerza el "Connection: close" de abajo: desactiva el pool de
        // conexiones keep-alive de HttpURLConnection a nivel de proceso,
        // que es la causa raíz documentada de los "Socket closed" al azar.
        System.setProperty("http.keepAlive", "false")
    }
    private const val TIMEOUT_MS = 30_000
    private const val MAX_ATTEMPTS = 3
    private val RETRY_DELAYS_MS = longArrayOf(2_000, 5_000)

    suspend fun generateText(
        prompt: String,
        maxOutputTokens: Int,
        jsonResponse: Boolean = false,
    ): String = withContext(Dispatchers.IO) {
        if (BuildConfig.GEMINI_API_KEY.isBlank() ||
            BuildConfig.GEMINI_API_KEY == "PASTE_YOUR_KEY_HERE"
        ) {
            throw CapsuleGenerationException("Falta configurar la API key de Gemini")
        }

        var lastError: Exception? = null
        for (attempt in 0 until MAX_ATTEMPTS) {
            try {
                return@withContext attemptRequest(prompt, maxOutputTokens, jsonResponse)
            } catch (e: RetryableServerException) {
                lastError = e
                if (attempt < RETRY_DELAYS_MS.size) delay(RETRY_DELAYS_MS[attempt])
            }
        }
        throw lastError ?: CapsuleGenerationException("Gemini no respondió tras varios intentos")
    }

    private fun attemptRequest(prompt: String, maxOutputTokens: Int, jsonResponse: Boolean): String {
        val requestBody = JSONObject().apply {
            put(
                "contents",
                JSONArray().put(
                    JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().apply { put("text", prompt) }))
                    },
                ),
            )
            put(
                "generationConfig",
                JSONObject().apply {
                    if (jsonResponse) put("responseMimeType", "application/json")
                    put("maxOutputTokens", maxOutputTokens)
                },
            )
        }

        val url = URL("$ENDPOINT?key=${BuildConfig.GEMINI_API_KEY}")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("content-type", "application/json")
            // HttpURLConnection en Android reutiliza conexiones HTTP keep-alive
            // de un pool interno, y hay un bug histórico conocido donde a veces
            // reutiliza un socket que el servidor ya cerró del otro lado,
            // provocando "Socket closed" / timeout en la lectura. Pedir que la
            // conexión se cierre al terminar evita que caiga en ese pool.
            setRequestProperty("Connection", "close")
            doOutput = true
            connectTimeout = TIMEOUT_MS
