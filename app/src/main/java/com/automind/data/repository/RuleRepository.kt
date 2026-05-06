package com.automind.data.repository

import com.automind.data.local.RuleDao
import com.automind.data.model.RuleEntity
import kotlinx.coroutines.flow.Flow

class RuleRepository(private val ruleDao: RuleDao) {
    val allRules: Flow<List<RuleEntity>> = ruleDao.getAllRules()

    suspend fun getActiveRules(): List<RuleEntity> = ruleDao.getActiveRules()

    suspend fun insert(rule: RuleEntity) {
        ruleDao.insertRule(rule)
    }

    suspend fun update(rule: RuleEntity) {
        ruleDao.updateRule(rule)
    }

    suspend fun delete(rule: RuleEntity) {
        ruleDao.deleteRule(rule)
    }
}
