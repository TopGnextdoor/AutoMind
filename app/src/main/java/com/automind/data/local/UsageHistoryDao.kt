package com.automind.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.automind.data.model.UsageHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageHistoryDao {
    @Query("SELECT * FROM usage_history ORDER BY timestamp DESC LIMIT 500")
    fun getRecentHistory(): Flow<List<UsageHistoryEntity>>

    @Query("SELECT * FROM usage_history")
    suspend fun getAllHistory(): List<UsageHistoryEntity>

    @Insert
    suspend fun insertHistory(history: UsageHistoryEntity)

    @Query("DELETE FROM usage_history WHERE timestamp < :threshold")
    suspend fun clearOldHistory(threshold: Long)
}
