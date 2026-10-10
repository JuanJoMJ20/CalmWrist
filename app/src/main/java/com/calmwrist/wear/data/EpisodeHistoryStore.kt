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
