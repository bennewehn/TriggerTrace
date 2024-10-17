package com.bennewehn.triggertrace.ui.symptoms

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Scale
import com.bennewehn.triggertrace.ui.components.NameInputField
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun AddSymptomScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: AddSymptomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.symptomAddedSuccessfully) {
        if (uiState.symptomAddedSuccessfully) {
            onBack()
        }
    }

    AddSymptomScreenContent(
        modifier = modifier,
        onBack = onBack,
        updateName = viewModel::updateName,
        uiState = uiState,
        onScaleUpdated = viewModel::setScale,
        onAddSymptom = viewModel::onAddSymptom,
        snackbarMessageShown = viewModel::snackbarMessageShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSymptomScreenContent(
    modifier: Modifier,
    onBack: () -> Unit,
    updateName: (String) -> Unit,
    onScaleUpdated: (Scale) -> Unit,
    onAddSymptom: () -> Unit,
    snackbarMessageShown: () -> Unit,
    uiState: AddSymptomUIState
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.add_symptom_title),
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
                NameInputField(name = uiState.name, onNameChanged = updateName)

                var expanded by remember { mutableStateOf(false) }

                val scaleText = mapOf(
                    Pair(Scale.NUMERIC, stringResource(id = R.string.one_to_ten)),
                    Pair(Scale.CATEGORICAL, stringResource(id = R.string.bad_medium_good)),
                    Pair(Scale.BINARY, stringResource(id = R.string.yes_or_no))
                )

                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .width(210.dp),
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded })
                {
                    OutlinedTextField(
                        readOnly = true,
                        value = scaleText[uiState.scale] ?: "",
                        onValueChange = {},
                        label = { Text(text = stringResource(id = R.string.scale)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        scaleText.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.value) },
                                onClick = {
                                    onScaleUpdated(it.key)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(30.dp),
                onClick = onAddSymptom,
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.add_food_save))
            }
        }
    }
}

@Preview(name = "Add Symptom Screen Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Add Symptom Screen Preview Light")
@Composable
private fun SettingsScreenPreview() {
    TriggerTraceTheme {
        AddSymptomScreenContent(
            modifier = Modifier,
            onBack = {},
            updateName = {},
            uiState = AddSymptomUIState(),
            onScaleUpdated = {},
            onAddSymptom = {},
            snackbarMessageShown = {}
        )
    }
}
