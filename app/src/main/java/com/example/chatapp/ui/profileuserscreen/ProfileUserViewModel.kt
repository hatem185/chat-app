package com.example.chatapp.ui.profileuserscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.User
import com.example.chatapp.model.profleuser.ProfileUserRepo
import com.example.chatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileUserViewModel @Inject constructor(private val repo: ProfileUserRepo) : ViewModel() {
    private val _profileStates = Channel<ProfileUserState>()
    val profileStates = _profileStates.receiveAsFlow()
    fun createOrUpdateUserProfile(user: User) {
        viewModelScope.launch {
            repo.createOrUpdateProfileUser(user).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _profileStates.send(ProfileUserState(isLoading = true))
                    }
                    is Resource.Error -> {
                        _profileStates.send(ProfileUserState(isError = result.message))
                    }
                    is Resource.Success -> {
                        _profileStates.send(ProfileUserState(isSuccess = true))
                    }
                }
            }
        }
    }
}