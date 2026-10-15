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
