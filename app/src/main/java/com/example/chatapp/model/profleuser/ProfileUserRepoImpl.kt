package com.example.chatapp.model.profleuser

import android.net.Uri
import com.example.chatapp.model.User
import com.example.chatapp.model.UserStatus
import com.example.chatapp.util.FirebaseConstants.CONTACTS_REFRNCE
import com.example.chatapp.util.FirebaseConstants.CONTACT_USER_EMAIL
import com.example.chatapp.util.FirebaseConstants.CONTACT_USER_NAME
import com.example.chatapp.util.FirebaseConstants.CONTACT_USER_UUID
import com.example.chatapp.util.FirebaseConstants.PROFILES_REFERENCE
import com.example.chatapp.util.FirebaseConstants.PROFILE_STATUS
import com.example.chatapp.util.FirebaseConstants.PROFILE_UUID
import com.example.chatapp.util.FirebaseConstants.USER_EMAIL
import com.example.chatapp.util.FirebaseConstants.USER_NAME
import com.example.chatapp.util.FirebaseConstants.USER_PHONE_NUMBER
import com.example.chatapp.util.FirebaseConstants.USER_PROFILE_PICTURE_URL
import com.example.chatapp.util.FirebaseConstants.USER_PROFILE_REFERNCE
import com.example.chatapp.util.FirebaseConstants.USER_SUR_NAME
import com.example.chatapp.util.MessageConstants.ERROR_MESSAGE
import com.example.chatapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*

class ProfileUserRepoImpl(
    private val fbAuth: FirebaseAuth,
    private val fbDb: FirebaseDatabase,
    private val fbStorage: FirebaseStorage
) : ProfileUserRepo {
    override suspend fun createOrUpdateProfileUser(user: User): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val userUUID = fbAuth.currentUser?.uid.toString()
            val userEmail = fbAuth.currentUser?.email.toString()
            val userProfileDBRef =
                fbDb.getReference(PROFILES_REFERENCE).child(userUUID).child(USER_PROFILE_REFERNCE)
            val contactsDbRef = fbDb.getReference(CONTACTS_REFRNCE)
            val childUpdates = mutableMapOf<String, Any>()
            childUpdates["/$PROFILE_UUID/"] = userUUID
            childUpdates["/$USER_EMAIL/"] = userEmail
            if (user.userName != "") childUpdates["/$USER_NAME/"] = user.userName
            if (user.userProfilePictureUrl != "") childUpdates["/$USER_PROFILE_PICTURE_URL/"] =
                user.userProfilePictureUrl
            if (user.userSurName != "") childUpdates["/$USER_SUR_NAME/"] = user.userSurName
            if (user.userPhoneNumber != "") childUpdates["/$USER_PHONE_NUMBER/"] =
                user.userPhoneNumber
            childUpdates["/$PROFILE_STATUS/"] = UserStatus.ONLINE.toString()
            userProfileDBRef.updateChildren(childUpdates).await()
            contactsDbRef.push()
                .setValue(
                    mapOf(
                        CONTACT_USER_UUID to userUUID,
                        CONTACT_USER_EMAIL to userEmail,
                        CONTACT_USER_NAME to user.userName
                    )
                )
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Error(it.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun uploadPictureToFirebase(url: Uri): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading())
            val uuidImage = UUID.randomUUID()
            val imageName = "images/$uuidImage.jpg"
            val storageRef = fbStorage.reference.child(imageName)
            storageRef.putFile(url).apply {}.await()
            var downloadUrl = ""
            storageRef.downloadUrl.addOnSuccessListener {
                downloadUrl = it.toString()
            }.await()
            emit(Resource.Success(downloadUrl))
        }.catch {
            emit(Resource.Error(it.message ?: ERROR_MESSAGE))
        }
    }

    override suspend fun loadProfileFromFirebase(): Flow<Resource<User>> {
        return callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Resource.Loading())
                val userUUID = fbAuth.currentUser?.uid
                val databaseReference = fbDb.getReference(PROFILES_REFERENCE)
                val postListener =
                    databaseReference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userFromFirebaseDatabase =
                                snapshot.child(userUUID!!).child(USER_PROFILE_REFERNCE)
                                    .getValue(User::class.java)
                                    ?: User()
                            this@callbackFlow.trySendBlocking(
                                Resource.Success(
                                    userFromFirebaseDatabase
                                )
                            )
                        }

                        override fun onCancelled(error: DatabaseError) {
                            this@callbackFlow.trySendBlocking(Resource.Error(error.message))
                        }
                    })
                databaseReference.addValueEventListener(postListener)
                awaitClose {
                    databaseReference.removeEventListener(postListener)
                    channel.close()
                    cancel()
                }
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Resource.Error(e.message ?: ERROR_MESSAGE))
            }
        }
    }

    override suspend fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            if (fbAuth.currentUser != null) {
                val userUUID = fbAuth.currentUser?.uid.toString()
                val databaseReference =
                    fbDb.getReference(PROFILES_REFERENCE).child(userUUID)
                        .child(USER_PROFILE_REFERNCE)
                        .child(PROFILE_STATUS)
                databaseReference.setValue(userStatus.toString()).await()
                emit(Resource.Success(true))
            } else {
                emit(Resource.Success(false))
            }
        }.catch {
            emit(Resource.Error(it.message ?: ERROR_MESSAGE))
        }

    }

}
