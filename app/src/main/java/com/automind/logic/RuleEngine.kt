package com.automind.logic

import com.automind.data.model.RuleEntity
import java.text.SimpleDateFormat
import java.util.*

class RuleEngine {
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun isRuleTriggered(rule: RuleEntity, currentApp: String): Boolean {
        if (!rule.isEnabled) return false
        if (rule.targetPackage != currentApp) return false

        val currentTime = timeFormat.format(Date())
        return isTimeBetween(currentTime, rule.startTime, rule.endTime)
    }

    private fun isTimeBetween(current: String, start: String, end: String): Boolean {
        val cur = timeFormat.parse(current)
        val st = timeFormat.parse(start)
        val en = timeFormat.parse(end)

        return if (st.before(en)) {
            cur.after(st) && cur.before(en)
        } else {
            // Over midnight case
            cur.after(st) || cur.before(en)
        }
    }
}
