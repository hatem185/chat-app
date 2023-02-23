package com.example.chatapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.auth.AuthRepo
import com.example.chatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: AuthRepo,
) : ViewModel() {
    private val _signUpStates = Channel<SignUpState>()
    val signUpStates = _signUpStates.receiveAsFlow()

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            repo.registerUser(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _signUpStates.send(SignUpState(isLoading = true))
                    }
                    is Resource.Error -> {
                        _signUpStates.send(SignUpState(isError = result.message))
                    }
                    is Resource.Success -> {
                        _signUpStates.send(SignUpState(isSuccess = true))
                    }
                }
            }
        }
    }

}