package com.calmwrist.wear.data

import android.content.Context
import com.calmwrist.wear.model.EpisodeRecord
import org.json.JSONArray
import org.json.JSONObject

/**
 * Historial de episodios resueltos, guardado local en el reloj. Es la
 * materia prima de la carta mensual: nunca sale del dispositivo salvo
 * cuando el propio usuario pide generar la carta (y ahí solo se mandan
 * los números agregados, no datos biométricos crudos).
 */
class EpisodeHistoryStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun append(record: EpisodeRecord) {
        val updated = load() + record
        val json = JSONArray()
        updated.forEach { r ->
            json.put(
                JSONObject().apply {
                    put("t", r.timestampMillis)
                    put("d", r.durationSeconds)
                },
            )
        }
        prefs.edit().putString(KEY_EPISODES, json.toString()).apply()
    }

    fun load(): List<EpisodeRecord> {
        val raw = prefs.getString(KEY_EPISODES, null) ?: return emptyList()
        val json = JSONArray(raw)
        return (0 until json.length()).map { i ->
            val obj = json.getJSONObject(i)
            EpisodeRecord(timestampMillis = obj.getLong("t"), durationSeconds = obj.getLong("d"))
        }
    }

    fun markLetterGenerated() {
        prefs.edit().putLong(KEY_LAST_LETTER_AT, System.currentTimeMillis()).apply()
    }

    fun lastLetterGeneratedAt(): Long? {
        val value = prefs.getLong(KEY_LAST_LETTER_AT, -1L)
        return if (value == -1L) null else value
    }

    /** Usado por el panel de control de demo para resetear el historial de episodios. */
    fun clear() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val PREFS_NAME = "calmwrist_episodes"
        const val KEY_EPISODES = "episodes"
        const val KEY_LAST_LETTER_AT = "last_letter_at"
    }
}
