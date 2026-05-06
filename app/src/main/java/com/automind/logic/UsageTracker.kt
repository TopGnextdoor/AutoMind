package com.automind.logic

import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.*

class UsageTracker(private val context: Context) {
    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    fun getForegroundApp(): String? {
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - 1000 * 10, // Check last 10 seconds
            time
        )
        if (stats != null) {
            val sortedStats = stats.sortedByDescending { it.lastTimeUsed }
            if (sortedStats.isNotEmpty()) {
                return sortedStats[0].packageName
            }
        }
        return null
    }

    fun getTodayUsage(): Map<String, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )
        
        val usageMap = mutableMapOf<String, Long>()
        stats?.forEach {
            if (it.totalTimeInForeground > 0) {
                usageMap[it.packageName] = it.totalTimeInForeground
            }
        }
        return usageMap
    }
}
