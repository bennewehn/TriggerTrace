package com.bennewehn.triggertrace.ui.diary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar

@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
){
    Scaffold(
        modifier = modifier,
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.diary_screen_title)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){

        }
    }
}
