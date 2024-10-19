package com.bennewehn.triggertrace.ui.settings

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.components.SettingsClickable
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onInfoClicked: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    //val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/x-sqlite3"),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.exportDatabase(uri) }
        }
    )

    val selectDirectoryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.exportCsvFilesToDirectory(it) }
        }
    )

    SettingsScreenContent(
        modifier = modifier,
        onBack = onBack,
        onInfoClicked = onInfoClicked,
        //uiState = uiState,
        //updateLogPollen = viewModel::updateLogPollen,
        //updateLogTemperature = viewModel::updateLogTemperature,
        exportDb = { createFileLauncher.launch("database.db") },
        onCsvClicked = { selectDirectoryLauncher.launch(null) }
    )
}

@Composable
private fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onInfoClicked: () -> Unit,
    onCsvClicked: () -> Unit,
    //uiState: SettingsUIState,
    //updateLogPollen: (Boolean) -> Unit,
    //updateLogTemperature: (Boolean) -> Unit,
    exportDb: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.settings_screen_title)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        )
        {

            SettingsClickable(
                icon = Icons.Rounded.IosShare,
                name = R.string.settings_export_database,
                onClick = exportDb
            )
            SettingsClickable(
                icon = ImageVector.vectorResource(id = R.drawable.csv_icon),
                name = R.string.export_as_csvs,
                onClick = onCsvClicked
            )
            /*
            SettingsSwitch(
                icon = Icons.Rounded.Grass,
                name = R.string.settings_log_pollen,
                state = uiState.logPollen,
                onChanged = updateLogPollen
            )
            SettingsSwitch(
                icon = Icons.Rounded.DeviceThermostat,
                name = R.string.settings_log_temperature,
                state = uiState.logTemperature,
                onChanged = updateLogTemperature
            )
             */
            SettingsClickable(
                icon = Icons.Rounded.Info,
                name = R.string.settings_info,
                onClick = onInfoClicked
            )
        }
    }
}

@Preview(name = "Settings Screen Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Settings Screen Preview Light")
@Composable
private fun SettingsScreenPreview() {
    TriggerTraceTheme {
        SettingsScreenContent(
            onBack = {},
            onInfoClicked = {},
            exportDb = {},
            onCsvClicked = {}
        )
    }
}