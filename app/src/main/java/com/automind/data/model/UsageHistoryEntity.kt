package com.automind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_history")
data class UsageHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val timestamp: Long, // When this was recorded
    val duration: Long,  // Duration in milliseconds
    val hourOfDay: Int   // 0-23 for easy pattern analysis
)
