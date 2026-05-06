package com.automind.data.local

import androidx.room.*
import com.automind.data.model.RuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao {
    @Query("SELECT * FROM rules")
    fun getAllRules(): Flow<List<RuleEntity>>

    @Query("SELECT * FROM rules WHERE isEnabled = 1")
    suspend fun getActiveRules(): List<RuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: RuleEntity)

    @Update
    suspend fun updateRule(rule: RuleEntity)

    @Delete
    suspend fun deleteRule(rule: RuleEntity)
}
