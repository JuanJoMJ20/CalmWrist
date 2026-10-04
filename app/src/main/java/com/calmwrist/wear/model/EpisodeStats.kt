package com.calmwrist.wear.model

import java.util.Calendar

enum class Trend { IMPROVING, STABLE, WORSENING, NOT_ENOUGH_DATA }

data class TimeBucket(val label: String, val count: Int)

data class EpisodeStats(
    val count: Int,
    val avgDurationSeconds: Long,
    val trend: Trend,
    val buckets: List<TimeBucket>,
)

/**
 * Agregados puros a partir del historial crudo — nada de esto toca IA ni
 * red: son solo los números que antes vivían sin usar en
 * [EpisodeHistoryStore]. La "tendencia" compara el promedio de los últimos
 * episodios contra los anteriores; con menos de 4 episodios no hay base
 * suficiente para decir nada, así que se marca explícitamente en vez de
 * inventar una tendencia con dos puntos de datos.
 */
fun computeEpisodeStats(episodes: List<EpisodeRecord>): EpisodeStats? {
    if (episodes.isEmpty()) return null

    val avg = episodes.map { it.durationSeconds }.average().toLong()

    val trend = if (episodes.size < 4) {
        Trend.NOT_ENOUGH_DATA
    } else {
        val sorted = episodes.sortedBy { it.timestampMillis }
        val splitAt = sorted.size / 2
        val olderAvg = sorted.take(splitAt).map { it.durationSeconds }.average()
        val recentAvg = sorted.drop(splitAt).map { it.durationSeconds }.average()
        val change = (recentAvg - olderAvg) / olderAvg
        when {
            change <= -0.15 -> Trend.IMPROVING
            change >= 0.15 -> Trend.WORSENING
            else -> Trend.STABLE
        }
    }

    val bucketLabels = listOf("Madrugada", "Mañana", "Tarde", "Noche")
    val calendar = Calendar.getInstance()
    val bucketCounts = IntArray(4)
    episodes.forEach { episode ->
        calendar.timeInMillis = episode.timestampMillis
