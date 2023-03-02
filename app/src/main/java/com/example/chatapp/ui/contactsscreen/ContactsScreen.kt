package com.example.chatapp.ui.contactsscreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.example.chatapp.R
import com.example.chatapp.model.Contact
import com.example.chatapp.model.ContactChat
import com.example.chatapp.ui.destinations.ChatRoomScreenDestination

import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ContactViwe(nav: DestinationsNavigator, viewModel: ContactsViewModel = hiltViewModel()) {
    val state = viewModel.contactsStates
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)

    ) {
        ContactsList(nav, viewModel)
    }
}

@Composable
fun ContactsList(nav: DestinationsNavigator, viewModel: ContactsViewModel) {
    val state = viewModel.contactsStates.collectAsState(initial = null)
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize(),

        ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            items(viewModel.contactList) { contact ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                        .clickable {
                            viewModel.isRoomChatExistsWith(contactUUID = contact.contactUserUUID)
                            if (viewModel.chatRoomUUID.isNotEmpty()) {
                                nav.navigate(
                                    ChatRoomScreenDestination(
                                        ContactChat(
                                            roomChatUIID = viewModel.chatRoomUUID,
                                            contact = contact
                                        )
                                    )
                                )
                                viewModel.resetChatRoomUIID()
                            }
                        }
                ) {
                    ContactItem(contact)
                    Divider()
                }

            }

        }
    }
}


@Composable
fun ContactItem(contact: Contact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(R.drawable.cat1),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Column() {
            Text(
                modifier = Modifier,
                text = contact.contactUserEmail,
                fontSize = 14.sp,
                color = Color.Black
            )

            Text(modifier = Modifier, text = "", fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.padding(85.dp))

        Text(modifier = Modifier, text = "", fontSize = 10.sp)

    }
}

