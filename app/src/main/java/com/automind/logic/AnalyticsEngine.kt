package com.automind.logic

import com.automind.data.model.UsageHistoryEntity
import java.util.*

class AnalyticsEngine {
    
    fun calculateDailyFocusScore(history: List<UsageHistoryEntity>, disciplineScore: Int): Int {
        if (history.isEmpty()) return disciplineScore
        
        val totalDuration = history.sumOf { it.duration }
        val focusDuration = history.filter { !isDistraction(it.packageName) }.sumOf { it.duration }
        
        val focusRatio = if (totalDuration > 0) focusDuration.toFloat() / totalDuration else 1f
        val weightedScore = (focusRatio * 70) + (disciplineScore * 0.3f)
        
        return weightedScore.toInt().coerceIn(0, 100)
    }

    fun getUsageByHour(history: List<UsageHistoryEntity>): Map<Int, Long> {
        return history.groupBy { it.hourOfDay }
            .mapValues { it.value.sumOf { entry -> entry.duration } }
    }

    fun getTopDistractions(history: List<UsageHistoryEntity>): List<Pair<String, Long>> {
        return history.filter { isDistraction(it.packageName) }
            .groupBy { it.packageName }
            .mapValues { it.value.sumOf { entry -> entry.duration } }
            .toList()
            .sortedByDescending { it.second }
            .take(5)
    }

    private fun isDistraction(packageName: String): Boolean {
        val distractionKeywords = listOf("social", "media", "video", "game", "instagram", "youtube", "tiktok", "facebook")
        return distractionKeywords.any { packageName.contains(it, ignoreCase = true) }
    }
}
