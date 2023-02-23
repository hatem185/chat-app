package com.example.chatapp.ui.chatroomscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import com.example.chatapp.R

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.chatapp.model.Contact
import com.example.chatapp.ui.contactsscreen.ContactsList
import com.example.chatapp.ui.contactsscreen.ContactsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun ChatRoomScreen(nav: DestinationsNavigator,contact: Contact) {
    Scaffold(
        topBar = {
            TopChatRoomBar(Contact())

        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MessagesContent(listOf<Contact>())
            }
        },
        bottomBar = {
            MessageSender()
        }
    )

}

@Composable
fun TopChatRoomBar(contact: Contact) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        backgroundColor = Color.White,
        navigationIcon = {
            Spacer(modifier = Modifier.width(10.dp))
            ButtonActionBar(
                icon = R.drawable.arrow_back_ios_24,
                onClickCallback = {},
                backColor = Color(0xFFF0EFEF)
            )
        },
        title = {
            ContactInfoBarItem(contact = contact)
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
fun ContactInfoBarItem(contact: Contact) {
    Row() {
        Image(
            painter = painterResource(R.drawable.cat1),
            contentDescription = "",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column() {
            Text(text = "Alberto Moedano", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = "Online now", fontSize = 12.sp, color = Color(0xFFFF9431))
        }
    }
}

@Composable
fun MessagesContent(contacts: List<Contact>) {
    LazyColumn() {
        items(count = 20) {
            MessageItem(Arrangement.End)
            Spacer(modifier = Modifier.height(10.dp))
            MessageItem(Arrangement.Start)
        }
    }
}

@Composable
fun MessageItem(arrang: Arrangement.Horizontal) {
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrang
    ) {
        Text(
            text = "aaaaffaaaaaffaaaaaffaaaaaaffaaaaaaaffaaaaa",
            modifier = Modifier
                .background(Color(0xFFFF9431))
                .width(180.dp)
                .padding(10.dp)

        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun MessageSender() {
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
                value = "Type somthing...",
                onValueChange = {},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ), modifier = Modifier.weight(1f)
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
                onClickCallback = { /*TODO*/ },
                backColor = Color(0xFFF6F6F6)
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}

