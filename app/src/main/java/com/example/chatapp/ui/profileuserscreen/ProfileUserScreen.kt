package com.example.chatapp.ui.profileuserscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.R
import com.example.chatapp.model.User
import com.example.chatapp.ui.commoncompos.CustomTextField
import com.example.chatapp.ui.destinations.LoginScreenDestination
import com.example.chatapp.ui.destinations.ProfileUserScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProfileUserScreen(
    nav: DestinationsNavigator,
    viewModel: ProfileUserViewModel = hiltViewModel()
) {
    val userName = rememberSaveable { mutableStateOf("") }
    val userProfilePictureUrl = rememberSaveable { mutableStateOf("") }
    val userSurName = rememberSaveable { mutableStateOf("") }
    val userPhoneNumber = rememberSaveable { mutableStateOf("") }
    val state = viewModel.profileStates.collectAsState(initial = null)
    val context = LocalContext.current.applicationContext
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,

        ) {
        Image(
            painter = painterResource(R.drawable.rectangle_8),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
        )
        {
            Card(
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .height(615.dp),
                shape = RoundedCornerShape(topStart = 90.dp),
                backgroundColor = Color.Yellow
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .height(615.dp)
                        .fillMaxWidth(), contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(315.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(45.dp))

                        Text(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(Color(0xffff9431)),
                            text = "Complete your Information", textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(45.dp))
                        CustomTextField(
                            textState = userName,
                            placeHolderText = "Enter your name",
                            leadingIcon = Icons.Default.Person
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        CustomTextField(
                            textState = userSurName,
                            placeHolderText = "Enter your sur name",
                            leadingIcon = Icons.Default.Person
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        CustomTextField(
                            textState = userProfilePictureUrl,
                            placeHolderText = "Upload your pecture profile",
                            leadingIcon = Icons.Default.Add
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        CustomTextField(
                            textState = userPhoneNumber,
                            placeHolderText = "Enter your phone number",
                            leadingIcon = Icons.Default.Phone
                        )
                        Spacer(modifier = Modifier.height(55.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    0xffff9431
                                )
                            ),
                            onClick = {
                                viewModel.createOrUpdateUserProfile(
                                    User(
                                        userName = userName.value,
                                        userSurName = userSurName.value,
                                        userPhoneNumber = userPhoneNumber.value,
                                        userProfilePictureUrl = userProfilePictureUrl.value
                                    )
                                )
                            }) {
                            Text(color = Color.White, text = "Save Profile", fontSize = 22.sp)
                        }
                        if (state.value?.isLoading == true) {
                            LinearProgressIndicator(color = Color(0xffff9431))
                        }
                        if (state.value?.isSuccess == true) {
                            Toast.makeText(
                                context,
                                "the user is created successfuly.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }
        }
    }
}