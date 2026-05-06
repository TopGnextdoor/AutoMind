package com.automind.logic

import com.automind.data.model.UsageHistoryEntity
import java.util.*

class PatternAnalyzer {
    
    /**
     * Finds hours of the day where distraction usage is significantly higher than average.
     * Returns a list of "Danger Zone" hours (0-23).
     */
    fun detectDangerZones(history: List<UsageHistoryEntity>): List<Int> {
        if (history.isEmpty()) return emptyList()

        val hourCounts = mutableMapOf<Int, Long>()
        history.forEach {
            hourCounts[it.hourOfDay] = (hourCounts[it.hourOfDay] ?: 0L) + it.duration
        }

        val avgDuration = hourCounts.values.average()
        return hourCounts.filter { it.value > avgDuration * 1.5 }.keys.toList()
    }

    /**
     * Detects if the user has a cycle of opening a specific app repeatedly.
     */
    fun isRecentDistractionCycle(history: List<UsageHistoryEntity>, packageName: String): Boolean {
        val recent = history.filter { it.packageName == packageName }.take(5)
        if (recent.size < 3) return false

        // Check if average gap between opens is less than 15 minutes
        var totalGap = 0L
        for (i in 0 until recent.size - 1) {
            totalGap += (recent[i].timestamp - recent[i+1].timestamp)
        }
        val avgGap = totalGap / (recent.size - 1)
        return avgGap < 15 * 60 * 1000 // 15 minutes
    }
}
