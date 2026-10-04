package com.calmwrist.wear.model

import java.util.Calendar

enum class Trend { IMPROVING, STABLE, WORSENING, NOT_ENOUGH_DATA }

data class TimeBucket(val label: String, val count: Int)

data class EpisodeStats(
    val count: Int,
    val avgDurationSeconds: Long,
    val trend: Trend,
