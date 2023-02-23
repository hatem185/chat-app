package com.example.chatapp.ui.login

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.auth.AuthRepo
import com.example.chatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewMode @Inject constructor(
    private val repo: AuthRepo,
) : ViewModel() {
    private val _loginStates = Channel<LoginInState>()
    val loginStates = _loginStates.receiveAsFlow()
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            repo.loginUser(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginStates.send(LoginInState(isLoading = true))
                    }
                    is Resource.Error -> {
                        _loginStates.send(LoginInState(isError = result.message.toString()))
                        loginStates.collect() { states ->
                            states.isError?.let { erorr ->
                                Log.e("LOGIN_ERROR", erorr)
                            }
                        }
                    }
                    is Resource.Success -> {
                        result.data?.user?.uid?.let { isProfileExists(it) }
                        print(result.toString())
                    }
                }
            }
        }

    }

    private fun isProfileExists(userId: String) {
        viewModelScope.launch {
            repo.checkUserProfileExists(userId).collect() { profileExists ->
                when (profileExists) {
                    is Resource.Loading -> {
                        _loginStates.send(LoginInState(isLoading = true))
                    }
                    is Resource.Error -> {
                        _loginStates.send(LoginInState(isError = profileExists.message))
                    }
                    is Resource.Success -> {
                        if (profileExists.data == true) {
                            _loginStates.send(LoginInState(isSuccess = true))
                        } else {
                            _loginStates.send(LoginInState(isProfileNotExists = true))
                        }
                    }
                }
            }
        }
    }
}