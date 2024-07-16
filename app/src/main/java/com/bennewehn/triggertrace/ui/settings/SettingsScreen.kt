package com.bennewehn.triggertrace.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.DeviceThermostat
import androidx.compose.material.icons.rounded.Grass
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.components.SettingsClickable
import com.bennewehn.triggertrace.ui.components.SettingsSwitch
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onInfoClicked: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreenContent(
        modifier = modifier,
        onBack = onBack,
        onInfoClicked = onInfoClicked,
        uiState = uiState,
        updateLogPollen = viewModel::updateLogPollen,
        updateLogTemperature = viewModel::updateLogTemperature
    )
}

@Composable
private fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onInfoClicked: () -> Unit,
    uiState: SettingsUIState,
    updateLogPollen: (Boolean) -> Unit,
    updateLogTemperature: (Boolean) -> Unit
){
    Scaffold(
        modifier = modifier,
        topBar = { SettingsTopAppBar(onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        )
        {
            SettingsClickable(icon = Icons.Rounded.IosShare, name = R.string.settings_export_database)
            SettingsSwitch(
                icon = Icons.Rounded.Grass,
                name = R.string.settings_log_pollen,
                state = uiState.logPollen,
                onChanged = updateLogPollen)
            SettingsSwitch(
                icon = Icons.Rounded.DeviceThermostat,
                name = R.string.settings_log_temperature,
                state = uiState.logTemperature,
                onChanged = updateLogTemperature)
            SettingsClickable(
                icon = Icons.Rounded.Info,
                name = R.string.settings_info,
                onClick = onInfoClicked)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopAppBar(onBack: () -> Unit){
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.settings_screen_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.back_button))
            }
        },
    )
}

@Preview(name = "Settings Screen Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Settings Screen Preview Light")
@Composable
private fun SettingsScreenPreview() {
    TriggerTraceTheme {
        SettingsScreenContent(
            onBack = {},
            onInfoClicked = {},
            uiState = SettingsUIState(),
            updateLogPollen = {},
            updateLogTemperature = {}
        )
    }
}