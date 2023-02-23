package com.example.chatapp.ui.contactsscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Contact
import com.example.chatapp.model.contacts.ContactsRepo
import com.example.chatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(private val repo: ContactsRepo) : ViewModel() {
    private val _contactsStates = Channel<ContactsState>()
    val contactsStates = _contactsStates.receiveAsFlow()
    var contactList by mutableStateOf(listOf<Contact>())
        private set
    var chatRoomUUID by mutableStateOf("")
        private set

    init {
        loadContactsList()
    }

    private fun loadContactsList() {
        viewModelScope.launch {
            repo.loadContactsList().collect() { result ->
                when (result) {
                    is Resource.Error -> {
                        _contactsStates.send(ContactsState(isError = result.message))
                    }
                    is Resource.Loading -> {
                        _contactsStates.send(ContactsState(isLoading = true))
                    }
                    is Resource.Success -> {
                        _contactsStates.send(ContactsState(isSuccess = true))
                        contactList = result.data!!
//                        _contactsStates.send(ContactsState(contactList = result.data!!))
                    }
                }
            }
        }
    }

    fun isRoomChatWithContactExists(requsterUUID: String) {
        viewModelScope.launch {
            repo.isRoomChatExists(requsterUUID).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _contactsStates.send(ContactsState(isError = result.message))
                    }
                    is Resource.Loading -> {
                        _contactsStates.send(ContactsState(isLoading = true))
                    }
                    is Resource.Success -> {
                        val chatRoomUUIDResult = result.data.toString()
                        if (chatRoomUUIDResult.isNotEmpty()) {
                            chatRoomUUID = chatRoomUUIDResult
                        } else {
                            createChatRoomWithContact(requsterUUID)
                        }
                    }
                }
            }

        }
    }

    private fun createChatRoomWithContact(requsterUUID: String) {
        viewModelScope.launch {
            repo.createChatRoomWithFrinde(requesterUUID = requsterUUID).collect() { result ->
                when (result) {
                    is Resource.Error -> {
                        _contactsStates.send(ContactsState(isError = result.message))
                    }
                    is Resource.Loading -> {
                        _contactsStates.send(ContactsState(isLoading = true))
                    }
                    is Resource.Success -> {
                        val chatRoomUUIDResult = result.data.toString()
                        chatRoomUUID = chatRoomUUIDResult
                        Log.d("CREATED_ROOM", "The room chat is created")
                    }
                }
            }
        }
    }
}