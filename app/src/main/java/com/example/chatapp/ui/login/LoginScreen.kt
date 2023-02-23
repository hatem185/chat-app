package com.example.chatapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.example.chatapp.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
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
import com.example.chatapp.ui.commoncompos.CustomTextField
import com.example.chatapp.ui.destinations.ContactViweDestination
import com.example.chatapp.ui.destinations.ProfileUserScreenDestination
import com.example.chatapp.ui.destinations.SignUpScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(nav: DestinationsNavigator, viewModel: LoginViewMode = hiltViewModel()) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val state = viewModel.loginStates.collectAsState(initial = null)
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
                        .fillMaxWidth(), contentAlignment = TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(315.dp),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(70.dp))
                        Text(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(Color(0xffff9431)),
                            text = "Log In",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(70.dp))
                        CustomTextField(
                            textState = email,
                            placeHolderText = "Enter your e-mail",
                            leadingIcon = Icons.Default.Email
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        CustomTextField(
                            textState = password,
                            placeHolderText = "Enter your passwordl",
                            leadingIcon = Icons.Default.Lock
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Forgot Password",
                            color = Color(0xffff9431),
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.height(100.dp))
                        Button(
                            modifier = Modifier
                                .height(58.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    0xffff9431
                                )
                            ),
                            onClick = {
                                viewModel.loginUser(email.value, password.value)
                            }) {
                            Text(color = Color.White, text = "Log In", fontSize = 22.sp)
                        }
                        TextButton(onClick = { nav.navigate(SignUpScreenDestination) })
                        {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = Color.Black)) {
                                        append("Don't have a account?  ")
                                    }
                                    append("Sing up here")
                                }, color = Color(0xffff9431)
                            )
                        }
                        if (state.value?.isLoading == true) LinearProgressIndicator()
                        if (state.value?.isProfileNotExists == true) {
                            nav.navigate(ProfileUserScreenDestination)
                        }
                        if (state.value?.isSuccess == true) {
                            nav.navigate(ContactViweDestination)
                        }
                    }
                }
            }
        }
    }
}
