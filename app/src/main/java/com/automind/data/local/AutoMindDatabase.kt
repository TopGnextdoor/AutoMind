package com.automind.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.automind.data.model.*
import com.automind.data.local.*

@Database(entities = [RuleEntity::class, UsageHistoryEntity::class, SuggestionEntity::class, AutopilotLogEntity::class], version = 4, exportSchema = false)
abstract class AutoMindDatabase : RoomDatabase() {
    abstract fun ruleDao(): RuleDao
    abstract fun usageHistoryDao(): UsageHistoryDao
    abstract fun suggestionDao(): SuggestionDao
    abstract fun autopilotDao(): AutopilotDao

    companion object {
        @Volatile
        private var INSTANCE: AutoMindDatabase? = null

        fun getDatabase(context: Context): AutoMindDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutoMindDatabase::class.java,
                    "automind_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
