package com.automind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rules")
data class RuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val targetPackage: String,
    val startTime: String, // HH:mm
    val endTime: String,   // HH:mm
    val isEnabled: Boolean = true,
    val actionType: String = "BLOCK" // BLOCK, WARN
)
