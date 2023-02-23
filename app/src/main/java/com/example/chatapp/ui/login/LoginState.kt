package com.example.chatapp.ui.login

data class LoginInState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isProfileNotExists: Boolean = false,
    val isError: String? = ""
)