package com.automind.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.automind.data.model.AutopilotLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AutopilotDao {
    @Query("SELECT * FROM autopilot_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<AutopilotLogEntity>>

    @Insert
    suspend fun insertLog(log: AutopilotLogEntity)

    @Query("DELETE FROM autopilot_logs")
    suspend fun clearLogs()
}
