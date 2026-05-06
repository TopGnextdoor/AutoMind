package com.automind.logic

import com.automind.data.model.UsageHistoryEntity
import java.util.*

data class HabitPattern(val packageName: String, val hourOfDay: Int, val confidence: Float)

class HabitLearner {
    
    /**
     * Identifies apps used consistently at specific hours.
     */
    fun findRecurringHabits(history: List<UsageHistoryEntity>): List<HabitPattern> {
        if (history.isEmpty()) return emptyList()

        // Group by app and hour, count days
        val appHourCounts = history.groupBy { it.packageName to it.hourOfDay }
        val habits = mutableListOf<HabitPattern>()

        appHourCounts.forEach { (key, entries) ->
            val uniqueDays = entries.map { 
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.timestamp
                cal.get(Calendar.DAY_OF_YEAR)
            }.distinct().size

            // If used on 3+ distinct days in history for this hour
            if (uniqueDays >= 3) {
                habits.add(HabitPattern(key.first, key.second, uniqueDays / 7f)) // 7-day normalized confidence
            }
        }
        
        return habits
    }
}
