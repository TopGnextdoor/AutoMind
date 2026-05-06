package com.automind.data.repository

import com.automind.data.model.RuleEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val rulesCollection get() = auth.currentUser?.uid?.let { 
        firestore.collection("users").document(it).collection("rules")
    }

    suspend fun syncRuleToCloud(rule: RuleEntity) {
        val uid = auth.currentUser?.uid ?: return
        val docData = mapOf(
            "title" to rule.title,
            "description" to rule.description,
            "targetPackage" to rule.targetPackage,
            "startTime" to rule.startTime,
            "endTime" to rule.endTime,
            "isEnabled" to rule.isEnabled
        )
        rulesCollection?.document(rule.id.toString())?.set(docData, SetOptions.merge())?.await()
    }

    suspend fun getRulesFromCloud(): List<RuleEntity> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        val snapshot = rulesCollection?.get()?.await()
        return snapshot?.documents?.mapNotNull { doc ->
            RuleEntity(
                id = doc.id.toIntOrNull() ?: 0,
                title = doc.getString("title") ?: "",
                description = doc.getString("description") ?: "",
                targetPackage = doc.getString("targetPackage") ?: "",
                startTime = doc.getString("startTime") ?: "",
                endTime = doc.getString("endTime") ?: "",
                isEnabled = doc.getBoolean("isEnabled") ?: true
            )
        } ?: emptyList()
    }
}
