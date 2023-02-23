package com.example.chatapp.model.contacts

import com.example.chatapp.model.Contact
import com.example.chatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ContactsRepo {
    suspend fun loadContactsList(): Flow<Resource<List<Contact>>>
    suspend fun isRoomChatExists(requesterUUID: String): Flow<Resource<String>>
    suspend fun createChatRoomWithFrinde(requesterUUID: String):Flow<Resource<String>>
}