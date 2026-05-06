package com.automind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "autopilot_logs")
data class AutopilotLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val action: String,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)
