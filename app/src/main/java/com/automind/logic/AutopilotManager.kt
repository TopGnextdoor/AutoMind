package com.automind.logic

import com.automind.data.model.UsageHistoryEntity

class AutopilotManager(private val analyzer: PatternAnalyzer) {
    
    fun getStrictnessLevel(disciplineScore: Int): Strictness {
        return when {
            disciplineScore > 80 -> Strictness.LENIENT
            disciplineScore > 40 -> Strictness.MODERATE
            else -> Strictness.AGGRESSIVE
        }
    }

    /**
     * Determines if an app should be blocked dynamically based on danger zones.
     */
    fun shouldAutoBlock(
        packageName: String,
        hourOfDay: Int,
        dangerZones: List<Int>,
        strictness: Strictness
    ): Boolean {
        // If in a danger zone, always block in AGGRESSIVE mode.
        // In MODERATE, block only if it's a known distraction.
        return when (strictness) {
            Strictness.AGGRESSIVE -> dangerZones.contains(hourOfDay)
            Strictness.MODERATE -> dangerZones.contains(hourOfDay) && isDistraction(packageName)
            Strictness.LENIENT -> false
        }
    }

    private fun isDistraction(packageName: String): Boolean {
        val socialKeywords = listOf("social", "media", "instagram", "facebook", "twitter", "tiktok", "youtube")
        return socialKeywords.any { packageName.contains(it, ignoreCase = true) }
    }

    enum class Strictness { LENIENT, MODERATE, AGGRESSIVE }
}
