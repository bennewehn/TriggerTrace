package com.bennewehn.triggertrace.ui.components

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigateBackTopAppBar(
    onBack: () -> Unit,
    title: String,
    ){
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.back_button))
            }
        },
    )
}

@Preview(name = "NavigateBackTopAppBar Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "NavigateBackTopAppBarPreview Light")
@Composable
private fun NavigateBackTopAppBarPreview(){
    TriggerTraceTheme {
        Surface{
            NavigateBackTopAppBar(
                onBack = {},
                title = "Title"
            )
        }
    }
}
