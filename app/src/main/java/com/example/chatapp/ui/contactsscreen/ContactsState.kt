package com.example.chatapp.ui.contactsscreen

import com.example.chatapp.model.Contact

data class ContactsState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = "",
    val isChatRoomExists: Boolean = false,
)