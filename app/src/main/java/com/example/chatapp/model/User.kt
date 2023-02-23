package com.example.chatapp.model

data class User(
    var profileUUID: String = "",
    var userEmail: String = "",
    var userName: String = "",
    var userProfilePictureUrl: String = "",
    var userSurName: String = "",
    var userPhoneNumber: String = "",
    var status: String = ""
)