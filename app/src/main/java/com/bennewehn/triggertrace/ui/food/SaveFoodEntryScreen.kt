package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import com.bennewehn.triggertrace.ui.components.DateInputField
import com.bennewehn.triggertrace.ui.components.DatePickerModal
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.components.SaveButton
import com.bennewehn.triggertrace.ui.components.SuccessfulDialog
import com.bennewehn.triggertrace.ui.components.TimeInputField
import com.bennewehn.triggertrace.ui.components.TimePickerDialog
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveFoodEntryScreen(
    onBack: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: SaveFoodEntryViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigateHome) {
        if(uiState.navigateHome){
            onNavigateHome()
        }
    }

    SaveFoodEntryScreenContent(
        onBack = onBack,
        uiState = uiState,
        onDismissSuccessfulDialog = viewModel::onDismissSuccessfulDialog,
        updateMinute = viewModel::updateMinute,
        updateHour = viewModel::updateHour,
        updateSelectedDate = viewModel::updateSelectedDate,
        onSaveToDiary = viewModel::onAddSelectedFood
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SaveFoodEntryScreenContent(
    onBack: () -> Unit,
    uiState: SaveFoodEntryUIState,
    onDismissSuccessfulDialog: () -> Unit,
    updateSelectedDate: (Date) -> Unit,
    updateHour: (Int) -> Unit,
    updateMinute: (Int) -> Unit,
    onSaveToDiary: () -> Unit
){

    Scaffold(
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.food_screen_title)
            )
        },
    ) { innerPadding ->

        var showDatePicker by remember { mutableStateOf(false) }
        var showTimePicker by remember { mutableStateOf(false) }

        if (uiState.showSuccessfulDialog) {
            SuccessfulDialog(onDismissSuccessfulDialog)
        }

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { dateMillis ->
                    dateMillis?.let {
                        updateSelectedDate(Date(it))
                    }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false })
        }

        if (showTimePicker){
            TimePickerDialog(
                onDismiss = { showTimePicker = false },
                onConfirm = {
                    showTimePicker = false
                    updateHour(it.hour)
                    updateMinute(it.minute)
                }
            )
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    DateInputField(
                        openDatePicker = { showDatePicker = true },
                        selectedDate = uiState.selectedDate
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    TimeInputField(
                        openTimePicker = { showTimePicker = true },
                        selectedTime = uiState.selectedDate
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SaveButton(onClick = onSaveToDiary)
                }
            }
        }
    }
}


@Preview(name = "Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Preview Light")
@Composable
private fun PreviewScreen() {
    TriggerTraceTheme {
        SaveFoodEntryScreenContent(
            onBack = {},
            onDismissSuccessfulDialog = {},
            updateHour = {},
            updateMinute = {},
            updateSelectedDate = {},
            uiState = SaveFoodEntryUIState(),
            onSaveToDiary = {}
        )
    }
}
