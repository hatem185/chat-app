package com.example.chatapp.ui.commoncompos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(
    textState: MutableState<String>,
    placeHolderText: String,
    leadingIcon: ImageVector
) {
    val corner = 8.dp
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Card(shape = RoundedCornerShape(corner)) {
        OutlinedTextField(
            modifier = Modifier
                .background(color = Color(0xfff6f7fb))
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = textState.value,
            textStyle = TextStyle(color = Color.Black),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xffff9431),
                unfocusedBorderColor = Color(0xfff6f7fb)
            ),
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "", tint = Color(0xff9A9A9A)
                )
            },
            onValueChange = { textState.value = it },
            placeholder = {
                Text(
                    style = TextStyle(Color(0xff9A9A9A)),
                    text = placeHolderText
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            })
        )

    }
}

