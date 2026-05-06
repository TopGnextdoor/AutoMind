package com.automind.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.automind.data.model.SuggestionEntity
import com.automind.data.model.SuggestionStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionDao {
    @Query("SELECT * FROM suggestions WHERE status = 'PENDING' ORDER BY timestamp DESC")
    fun getPendingSuggestions(): Flow<List<SuggestionEntity>>

    @Insert
    suspend fun insertSuggestion(suggestion: SuggestionEntity)

    @Update
    suspend fun updateSuggestion(suggestion: SuggestionEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM suggestions WHERE packageName = :pkg AND hourOfDay = :hour AND status != 'PENDING')")
    suspend fun hasBeenProcessed(pkg: String, hour: Int): Boolean
}
