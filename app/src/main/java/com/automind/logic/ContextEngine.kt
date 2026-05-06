package com.automind.logic

import com.automind.data.model.UsageHistoryEntity
import java.util.*

class ContextEngine(private val analyzer: PatternAnalyzer) {

    fun shouldTriggerSmartIntervention(
        history: List<UsageHistoryEntity>,
        currentPackage: String
    ): Pair<Boolean, String?> {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val dangerZones = analyzer.detectDangerZones(history)

        // Rule 1: Predictive Warning - About to enter a high distraction zone
        if (dangerZones.contains(currentHour)) {
            return true to "You usually lose focus around this time. Stay mindful!"
        }

        // Rule 2: Distraction Cycle - Repeatedly opening the same app
        if (analyzer.isRecentDistractionCycle(history, currentPackage)) {
            return true to "This is your 3rd time opening this app recently. Take a break?"
        }

        return false to null
    }
}
