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

    fun resetChatRoomUIID() {
        chatRoomUUID = ""
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

    fun isRoomChatExistsWith(contactUUID: String) {
        viewModelScope.launch {
            repo.isRoomChatExists(contactUUID).collect { result ->
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
                            Log.d("UUID", chatRoomUUIDResult)
                        } else {
                            createChatRoomWithContact(contactUUID)
                        }
                    }
                }
            }

        }
    }

    private fun createChatRoomWithContact(contactUUID: String) {
        viewModelScope.launch {
            repo.createChatRoomWithFrinde(contactUUID = contactUUID).collect() { result ->
                when (result) {
                    is Resource.Error -> {
                        _contactsStates.send(ContactsState(isError = result.message))
                    }
                    is Resource.Loading -> {
                        _contactsStates.send(ContactsState(isLoading = true))
                    }
                    is Resource.Success -> {
                        val chatRoomUUIDResult = result.data.toString()
                        Log.d("CREATED_ROOM", "The room chat is created")
                        Log.d("UUID", chatRoomUUIDResult)
                        if (chatRoomUUIDResult.isNotEmpty())
                            chatRoomUUID = chatRoomUUIDResult
                        else
                            _contactsStates.send(ContactsState(isLoading = true))
                    }
                }
            }
        }
    }
}
