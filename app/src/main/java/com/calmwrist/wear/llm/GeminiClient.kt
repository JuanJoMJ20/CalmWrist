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
