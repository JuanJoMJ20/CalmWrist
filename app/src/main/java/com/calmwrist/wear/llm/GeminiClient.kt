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

