package com.example.chatapp.ui.chatroomscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.chatrooms.ChatRoomsRepo
import com.example.chatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(private val repo: ChatRoomsRepo) : ViewModel() {
    private val _chatRoomState = Channel<ChatRoomState>()
    val chatRoomState = _chatRoomState.receiveAsFlow()


    fun loadContctMessagesRoom(chatRoomUUID: String) {
        viewModelScope.launch {
            repo.loadMessagesOf(chatRoomUUID).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _chatRoomState.send(ChatRoomState(isError = result.message))
                    }
                    is Resource.Loading -> {
                        _chatRoomState.send(ChatRoomState(isLoading = true))
                    }
                    is Resource.Success -> {
                        _chatRoomState.send(ChatRoomState(messageList = result.data!!))
                    }
                }
                Log.d("IN_LOAD_MESSAGE", result.data.toString())
            }
        }
    }

    fun sendMessage(
        message: String,
        status: String,
        roomChatUIID: String,
        freeTextField: () -> Unit
    ) {
        viewModelScope.launch {
            repo.sendMessage(message = message, status = status, roomChatUIID = roomChatUIID)
                .collect {result->
                    if (result.data == true){
                        loadContctMessagesRoom(roomChatUIID)
                        freeTextField()
                    }
                }

        }
    }
}