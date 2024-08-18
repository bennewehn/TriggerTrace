package com.bennewehn.triggertrace.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.bennewehn.triggertrace.R

@Composable
fun NameInputField(
    name: String,
    onNameChanged: (String) -> Unit,
){

    val focusRequester = remember { FocusRequester() }
    var hasFocused by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(hasFocused) {
        if (!hasFocused) {
            focusRequester.requestFocus()
            hasFocused = true
        }
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = name,
        onValueChange = onNameChanged,
        placeholder = {
            Text(
                text = stringResource(id = R.string.name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 1f)
        ),
    )
}