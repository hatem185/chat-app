package com.example.chatapp.ui.chatroomscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.example.chatapp.R

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.model.ChatMessage
import com.example.chatapp.model.ContactChat

import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ChatRoomScreen(
    nav: DestinationsNavigator,
    viewModel: ChatRoomViewModel = hiltViewModel(),
    contactChat: ContactChat
) {
    val state = viewModel.chatRoomState.collectAsState(initial = null)
    Scaffold(
        topBar = {
            TopChatRoomBar(nav, contactChat)
        },
        content = { paddingValues ->
            viewModel.loadContctMessagesRoom(contactChat.roomChatUIID)
            Box(modifier = Modifier.padding(paddingValues)) {
                MessagesContent(state, contactChat.contact.contactUserUUID)
            }
        },
        bottomBar = {
            MessageSender(contactChat.roomChatUIID, viewModel)
        }
    )
}

@Composable
fun TopChatRoomBar(nav: DestinationsNavigator, contactChat: ContactChat) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        backgroundColor = Color.White,
        navigationIcon = {
            Spacer(modifier = Modifier.width(10.dp))
            ButtonActionBar(
                icon = R.drawable.arrow_back_ios,
                onClickCallback = { nav.popBackStack() },
                backColor = Color(0xFFF0EFEF)
            )
        },
        title = {
            ContactInfoBarItem(contactChat = contactChat)
        },
        actions = {
            ButtonActionBar(
                icon = R.drawable.videocam_24,
                onClickCallback = {},
                backColor = Color(0xFFF0EFEF)
            )
            Spacer(modifier = Modifier.width(20.dp))
            ButtonActionBar(
                icon = R.drawable.phone_call_24,
                onClickCallback = {},
                backColor = Color(0xFFF0EFEF)
            )
            Spacer(modifier = Modifier.width(10.dp))

        }
    )
}

@Composable
fun ButtonActionBar(icon: Int, onClickCallback: () -> Unit, backColor: Color) {
    Card(shape = RoundedCornerShape(10.dp)) {

        IconButton(
            onClick = onClickCallback,
            modifier = Modifier
                .size(38.dp)
                .background(backColor)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "", tint = Color(0xFFEB7A30)
            )

        }
    }
}

@Composable
fun ContactInfoBarItem(contactChat: ContactChat) {
    Row() {
        Image(
            painter = painterResource(R.drawable.cat1),
            contentDescription = "",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column() {
            Text(text = contactChat.contact.contactUserName, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = "Online now", fontSize = 12.sp, color = Color(0xFFFF9431))
        }
    }
}

@Composable
fun MessagesContent(state: State<ChatRoomState?>, contactUserUUID: String) {
    LazyColumn() {
        state.value?.messageList?.let {
            items(it) { message ->
                if (message.profileUUID == contactUserUUID)
                    MessageItem(Arrangement.Start, message)
                else
                    MessageItem(Arrangement.End, message)
            }
        }

    }
}

@Composable
fun MessageItem(arrang: Arrangement.Horizontal, message: ChatMessage) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrang
    ) {
        Text(
            text = message.message,
            modifier = Modifier
                .background(Color(0xFFFF9431))
                .width(180.dp)
                .padding(10.dp)

        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun MessageSender(roomChatUIID: String, viewModel: ChatRoomViewModel) {
    var messageText by remember { mutableStateOf("") }
    BottomAppBar(backgroundColor = Color(0xFFF3F3F3)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            ButtonActionBar(
                icon = R.drawable.add_24,
                onClickCallback = { /*TODO*/ },
                backColor = Color(0xFFF6F6F6)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Divider(
                thickness = 1.dp,
                modifier = Modifier
                    .height(45.dp)
                    .width(1.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ), modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Type somthing...") }
            )
            Spacer(modifier = Modifier.width(15.dp))
            Divider(
                thickness = 1.dp,
                modifier = Modifier
                    .height(45.dp)
                    .width(1.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            ButtonActionBar(
                icon = R.drawable.arrow_up_24,
                onClickCallback = {
                    if (messageText.trim().isEmpty()) return@ButtonActionBar
                    viewModel.sendMessage(
                        message = messageText.trim(),
                        status = "Read",
                        roomChatUIID = roomChatUIID
                    ) {
                        messageText = ""
                    }

                },
                backColor = Color(0xFFF6F6F6)
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}

