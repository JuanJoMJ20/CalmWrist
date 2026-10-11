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

    fun recordCapsule(answers: List<String>, title: String?, subtitle: String?, error: String?) {
        prefs.edit()
            .putString(KEY_CAPSULE_ANSWERS, JSONArray(answers).toString())
            .putString(KEY_CAPSULE_TITLE, title)
            .putString(KEY_CAPSULE_SUBTITLE, subtitle)
            .putString(KEY_CAPSULE_ERROR, error)
            .putLong(KEY_CAPSULE_AT, System.currentTimeMillis())
            .apply()
    }

    fun recordLetter(episodeCount: Int, text: String?, error: String?) {
        prefs.edit()
            .putInt(KEY_LETTER_EPISODES, episodeCount)
            .putString(KEY_LETTER_TEXT, text)
            .putString(KEY_LETTER_ERROR, error)
            .putLong(KEY_LETTER_AT, System.currentTimeMillis())
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun toJson(): JSONObject = JSONObject().apply {
        val capsuleAt = prefs.getLong(KEY_CAPSULE_AT, -1L)
        if (capsuleAt != -1L) {
            put(
                "lastCapsule",
                JSONObject().apply {
                    put("answers", JSONArray(prefs.getString(KEY_CAPSULE_ANSWERS, "[]")))
                    put("title", prefs.getString(KEY_CAPSULE_TITLE, null))
                    put("subtitle", prefs.getString(KEY_CAPSULE_SUBTITLE, null))
                    put("error", prefs.getString(KEY_CAPSULE_ERROR, null))
                    put("at", capsuleAt)
                },
            )
        }
        val letterAt = prefs.getLong(KEY_LETTER_AT, -1L)
        if (letterAt != -1L) {
            put(
                "lastLetter",
                JSONObject().apply {
                    put("episodeCount", prefs.getInt(KEY_LETTER_EPISODES, 0))
                    put("text", prefs.getString(KEY_LETTER_TEXT, null))
                    put("error", prefs.getString(KEY_LETTER_ERROR, null))
                    put("at", letterAt)
                },
            )
        }
    }

    private companion object {
        const val PREFS_NAME = "calmwrist_ai_debug"
        const val KEY_CAPSULE_ANSWERS = "capsule_answers"
        const val KEY_CAPSULE_TITLE = "capsule_title"
        const val KEY_CAPSULE_SUBTITLE = "capsule_subtitle"
        const val KEY_CAPSULE_ERROR = "capsule_error"
        const val KEY_CAPSULE_AT = "capsule_at"
        const val KEY_LETTER_EPISODES = "letter_episodes"
        const val KEY_LETTER_TEXT = "letter_text"
        const val KEY_LETTER_ERROR = "letter_error"
        const val KEY_LETTER_AT = "letter_at"
    }
}
