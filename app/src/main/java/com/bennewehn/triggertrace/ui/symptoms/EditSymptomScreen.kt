package com.bennewehn.triggertrace.ui.symptoms

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.ui.components.NameInputField
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import com.bennewehn.triggertrace.R
import androidx.compose.runtime.getValue


@Composable
fun EditSymptomScreen(
    onBack: () -> Unit,
    viewModel: EditSymptomViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.symptomEditedSuccessfully) {
        if (uiState.symptomEditedSuccessfully) {
            onBack()
        }
    }

    Content(
        onBack = onBack,
        updateName = viewModel::updateName,
        uiState = uiState,
        onEditSymptom = viewModel::updateSymptom,
        snackbarMessageShown = viewModel::snackbarMessageShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    onBack: () -> Unit,
    updateName: (String) -> Unit,
    onEditSymptom: () -> Unit,
    snackbarMessageShown: () -> Unit,
    uiState: EditSymptomUIState
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.edit_symptom),
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(id = userMessage)
            LaunchedEffect(snackbarHostState, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(
                    message = snackbarText,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
                snackbarMessageShown()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                NameInputField(
                    name = uiState.name,
                    onNameChanged = updateName,
                    shouldFocus = false
                )
            }

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(30.dp),
                onClick = onEditSymptom,
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.add_food_save))
            }
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light")
@Composable
private fun Preview() {
    TriggerTraceTheme {
        Content(
            onBack = {},
            updateName = {},
            uiState = EditSymptomUIState(),
            onEditSymptom = {},
            snackbarMessageShown = {}
        )
    }
}
