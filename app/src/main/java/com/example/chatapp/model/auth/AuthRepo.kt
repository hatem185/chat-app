package com.example.chatapp.model.auth

import com.example.chatapp.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow


interface AuthRepo {
    suspend fun loginUser(email: String, passwor: String): Flow<Resource<AuthResult>>
    suspend fun registerUser(email: String, passwor: String): Flow<Resource<AuthResult>>
    suspend fun signOut(): Flow<Resource<Boolean>>
    suspend fun checkUserProfileExists(
        userId: String,
    ): Flow<Resource<Boolean>>

}