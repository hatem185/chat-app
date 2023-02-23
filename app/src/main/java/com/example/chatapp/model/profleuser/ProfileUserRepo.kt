package com.example.chatapp.model.profleuser
import android.net.Uri
import com.example.chatapp.model.User
import com.example.chatapp.model.UserStatus
import com.example.chatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileUserRepo {

    suspend fun createOrUpdateProfileUser(user:User): Flow<Resource<Boolean>>
    suspend fun uploadPictureToFirebase(url: Uri): Flow<Resource<String>>
    suspend fun loadProfileFromFirebase(): Flow<Resource<User>>
    suspend fun setUserStatusToFirebase(userStatus: UserStatus): Flow<Resource<Boolean>>
}