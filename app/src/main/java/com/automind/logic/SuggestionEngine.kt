package com.automind.logic

import com.automind.data.local.SuggestionDao
import com.automind.data.model.SuggestionEntity
import com.automind.data.model.UsageHistoryEntity

class SuggestionEngine(
    private val learner: HabitLearner,
    private val suggestionDao: SuggestionDao
) {
    suspend fun analyzeAndGenerateSuggestions(history: List<UsageHistoryEntity>): List<SuggestionEntity> {
        val habits = learner.findRecurringHabits(history)
        val newSuggestions = mutableListOf<SuggestionEntity>()

        for (habit in habits) {
            // Only suggest if not already processed (accepted/rejected)
            if (!suggestionDao.hasBeenProcessed(habit.packageName, habit.hourOfDay)) {
                val appName = habit.packageName.split(".").last().replaceFirstChar { it.uppercase() }
                val timeStr = formatHour(habit.hourOfDay)
                
                val suggestion = SuggestionEntity(
                    packageName = habit.packageName,
                    hourOfDay = habit.hourOfDay,
                    message = "You use $appName daily at $timeStr. Add a focus rule?"
                )
                newSuggestions.add(suggestion)
                suggestionDao.insertSuggestion(suggestion)
            }
        }
        return newSuggestions
    }

    private fun formatHour(hour: Int): String {
        return when {
            hour == 0 -> "12 AM"
            hour < 12 -> "$hour AM"
            hour == 12 -> "12 PM"
            else -> "${hour - 12} PM"
        }
    }
}
