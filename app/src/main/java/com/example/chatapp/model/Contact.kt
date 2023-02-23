package com.example.chatapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val contactUserUUID: String = "",
    val contactUserEmail: String = "",
    val contactUserName: String = ""
) : Parcelable