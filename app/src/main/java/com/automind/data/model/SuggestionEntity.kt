package com.automind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SuggestionStatus { PENDING, ACCEPTED, REJECTED }

@Entity(tableName = "suggestions")
data class SuggestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val hourOfDay: Int,
    val message: String,
    val status: SuggestionStatus = SuggestionStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)
