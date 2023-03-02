package com.example.chatapp.model.chatrooms

import com.example.chatapp.model.ChatMessage
import com.example.chatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRoomsRepo {
    suspend fun loadMessagesOf(chatRoomUUID: String): Flow<Resource<List<ChatMessage>>>
    suspend fun sendMessage(
        message: String,
        status: String,
        roomChatUIID: String
    ): Flow<Resource<Boolean>>
}