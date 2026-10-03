package com.calmwrist.wear.model

/**
 * Un episodio de crisis ya resuelto. Guarda lo mínimo necesario para que
 * la carta mensual pueda hablar de patrones (hora del día, cuánto tardó
 * en bajar el pulso) sin acumular datos de salud más sensibles de los
 * que hacen falta.
 */
data class EpisodeRecord(
    val timestampMillis: Long,
    val durationSeconds: Long,
)
