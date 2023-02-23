package com.example.chatapp.ui.chatroomscreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class ChatRoomViewModel : ViewModel() {
    private val _chatRoomState = Channel<ChatRoomState>()
    val chatRoomState = _chatRoomState.receiveAsFlow()

    fun loadContctMessagesRoom(chatRoomUUID: String) {

    }
}