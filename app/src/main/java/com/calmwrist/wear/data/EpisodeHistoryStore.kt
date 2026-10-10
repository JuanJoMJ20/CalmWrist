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
