package com.automind.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "autopilot_prefs")

class AutopilotPreferences(private val context: Context) {
    companion object {
        val AUTOPILOT_ENABLED = booleanPreferencesKey("autopilot_enabled")
        val DISCIPLINE_SCORE = intPreferencesKey("discipline_score")
    }

    val isAutopilotEnabled: Flow<Boolean> = context.dataStore.data.map { 
        it[AUTOPILOT_ENABLED] ?: false 
    }

    val disciplineScore: Flow<Int> = context.dataStore.data.map { 
        it[DISCIPLINE_SCORE] ?: 100 
    }

    suspend fun setAutopilotEnabled(enabled: Boolean) {
        context.dataStore.edit { it[AUTOPILOT_ENABLED] = enabled }
    }

    suspend fun updateDisciplineScore(delta: Int) {
        context.dataStore.edit { 
            val current = it[DISCIPLINE_SCORE] ?: 100
            it[DISCIPLINE_SCORE] = (current + delta).coerceIn(0, 100)
        }
    }
}
