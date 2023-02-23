package com.example.chatapp.ui.chatroomscreen

import com.example.chatapp.model.Contact

data class ChatRoomState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = "",
)