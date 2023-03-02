package com.example.chatapp.model

data class ChatMessage(
    val profileUUID: String = "",
    var message: String = "",
    var date: Long = System.currentTimeMillis(),
    var status: String = ""
)