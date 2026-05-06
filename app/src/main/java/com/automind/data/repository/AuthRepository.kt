package com.automind.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    
    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    init {
        auth.addAuthStateListener {
            _currentUser.value = it.currentUser
        }
    }

    fun signOut() {
        auth.signOut()
    }

    // Email login/signup and Google Sign-In logic would be implemented here
    // For this demonstration, we focus on the structure and observer
}
