package com.calmwrist.wear.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Registra la última interacción con Gemini en cada flujo (qué se mandó,
 * qué devolvió o qué error dio) solo para que el panel de control de la
 * demo pueda mostrarle al profesor que la IA realmente está respondiendo
 * y no es texto fijo. No se usa en el flujo normal de la app.
 */
class AiDebugStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

