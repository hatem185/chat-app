package com.example.chatapp.model.auth

import android.util.Log
import com.example.chatapp.util.FirebaseConstants.PROFILES_REFERENCE
import com.example.chatapp.util.MessageConstants.ERROR_MESSAGE
import com.example.chatapp.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val fbAuth: FirebaseAuth,
    private val fbDb: FirebaseDatabase
) : AuthRepo {
    override suspend fun loginUser(email: String, passwor: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = fbAuth.signInWithEmailAndPassword(email, passwor).await()
            emit(Resource.Success(result))
        }.catch { emit(Resource.Error(message = it.message.toString())) }
    }

    override suspend fun registerUser(email: String, passwor: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = fbAuth.createUserWithEmailAndPassword(email, passwor).await()
            emit(Resource.Success(result))
        }.catch { emit(Resource.Error(message = it.message.toString())) }
    }

    override suspend fun signOut(): Flow<Resource<Boolean>> {
        return callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Resource.Loading())
                fbAuth.signOut().apply {
                    this@callbackFlow.trySendBlocking(Resource.Success(true))
                }
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Resource.Error(e.message ?: ERROR_MESSAGE))
            }
            awaitClose {
                channel.close()
                cancel()
            }
        }
    }

    override suspend fun checkUserProfileExists(
        userId: String,

        ): Flow<Resource<Boolean>> {
        return callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Resource.Loading())
                val profilesRef = fbDb.getReference(PROFILES_REFERENCE).child(userId)
                val profileListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val profileExists = snapshot.exists()
                        Log.d("WELCOME","GGHHGGHH $profileExists")
                        this@callbackFlow.trySendBlocking(
                            Resource.Success(
                                profileExists
                            )
                        )
                    }
                    override fun onCancelled(e: DatabaseError) {
                        this@callbackFlow.trySendBlocking(Resource.Error(e.message))
                    }
                }
                profilesRef.addListenerForSingleValueEvent(profileListener)
                awaitClose {
                    profilesRef.removeEventListener(profileListener)
                    channel.close()
                    cancel()
                }
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Resource.Error(e.message ?: ERROR_MESSAGE))
            }
        }
    }
}