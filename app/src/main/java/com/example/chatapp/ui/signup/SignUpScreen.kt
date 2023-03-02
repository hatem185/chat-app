package com.example.chatapp.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.ui.destinations.LoginScreenDestination
import com.example.chatapp.R
import com.example.chatapp.ui.commoncompos.CustomTextField
import com.example.chatapp.ui.destinations.ProfileUserScreenDestination

import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun SignUpScreen(nav: DestinationsNavigator, viewModel: SignUpViewModel = hiltViewModel()) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val state = viewModel.signUpStates.collectAsState(initial = null)
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
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(315.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(60.dp))

                        Text(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(Color(0xffff9431)),
                            text = "Sing Up"
                        )

                        Spacer(modifier = Modifier.height(60.dp))
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
                        Spacer(modifier = Modifier.height(80.dp))


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
                                viewModel.registerUser(email.value, password.value)
                            }) {
                            Text(color = Color.White, text = "Sing Up", fontSize = 22.sp)
                        }
                        TextButton(onClick = { nav.navigate(LoginScreenDestination) }
                        )
                        {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = Color.Black)) {
                                        append("Do you have a account?  ")
                                    }
                                    append("Login here")
                                }, color = Color(0xffff9431)
                            )
                        }
                        if (state.value?.isLoading == true)
                            LinearProgressIndicator(color = Color(0xffff9431))
                        if (state.value?.isSuccess == true) nav.navigate(
                            ProfileUserScreenDestination
                        )
                    }
                }
            }
        }
    }
}