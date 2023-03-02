package com.example.chatapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactChat(
    val roomChatUIID: String = "",
    val contact: Contact = Contact()
) : Parcelable
