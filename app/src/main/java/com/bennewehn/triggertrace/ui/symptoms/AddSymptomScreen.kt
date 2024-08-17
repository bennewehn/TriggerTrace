package com.bennewehn.triggertrace.ui.symptoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bennewehn.triggertrace.R

@Composable
fun AddSymptomScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
){
    Scaffold(
        modifier = modifier,
        topBar = { AddSymptomTopAppBar(onBack) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSymptomTopAppBar(onBack: () -> Unit){
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.add_symptom_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.back_button))
            }
        },
    )
}