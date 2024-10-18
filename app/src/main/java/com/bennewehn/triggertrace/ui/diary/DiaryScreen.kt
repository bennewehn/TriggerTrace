package com.bennewehn.triggertrace.ui.diary

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Entry
import com.bennewehn.triggertrace.data.FoodEntry
import com.bennewehn.triggertrace.data.Scale
import com.bennewehn.triggertrace.data.Symptom
import com.bennewehn.triggertrace.data.SymptomEntry
import com.bennewehn.triggertrace.ui.components.DateInputField
import com.bennewehn.triggertrace.ui.components.DatePickerModal
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import com.bennewehn.triggertrace.utils.to24HourTime
import java.util.Date

@Composable
fun DiaryScreen(
    onBack: () -> Unit,
    viewModel: DiaryViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        onBack = onBack,
        uiState = uiState,
        updateSelectedDate = viewModel::updateSelectedDate,
        increaseDay = viewModel::increaseOneDay,
        decreaseDay = viewModel::decreaseOneDay,
        deleteEntry = viewModel::deleteEntry,
        undoDeletion = viewModel::undoDeletion,
        showNextMessage = viewModel::showNextMessage
    )
}

@Composable
private fun Content(
    onBack: () -> Unit,
    updateSelectedDate: (Date) -> Unit,
    increaseDay: () -> Unit,
    decreaseDay: () -> Unit,
    deleteEntry: (Entry, String) -> Unit,
    showNextMessage: () -> Unit,
    undoDeletion: (Entry) -> Unit,
    uiState: DiaryUIState
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val undoLabel = stringResource(id = R.string.UNDO)

    LaunchedEffect(uiState.messageQueue.firstOrNull()) {
        if (uiState.messageQueue.isNotEmpty()) {

            val result = snackbarHostState.showSnackbar(
                message = uiState.messageQueue.first().first,
                duration = SnackbarDuration.Short,
                actionLabel = undoLabel,
                withDismissAction = true
            )

            if (result == SnackbarResult.ActionPerformed) {
                undoDeletion(uiState.messageQueue.first().second)
            }
            showNextMessage()
        }
    }

    Scaffold(
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.diary_screen_title)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            var showDatePicker by remember { mutableStateOf(false) }

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                IconButton(
                    onClick = decreaseDay,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Previous",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                DateInputField(
                    openDatePicker = { showDatePicker = true },
                    selectedDate = uiState.selectedDate,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterVertically)
                )

                IconButton(
                    onClick = increaseDay,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if(uiState.listEntries.isEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No entries for this day.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 18.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }


            LazyColumn {
                items(uiState.listEntries.count()) { index ->
                    val iName = uiState.listEntries[index].second
                    val entry = uiState.listEntries[index].first

                    val dismissState = rememberSwipeToDismissBoxState()

                    LaunchedEffect(dismissState.currentValue) {
                        if(dismissState.currentValue == SwipeToDismissBoxValue.EndToStart){
                            dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                            deleteEntry(entry, iName.name)
                        }
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            val colorState = animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.Settled -> Color.Gray
                                    SwipeToDismissBoxValue.StartToEnd -> Color.DarkGray
                                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                                },
                                label = "Color Animation"
                            )
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(colorState.value)
                            ) {
                                // Show the delete icon when swiping from end to start
                                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        modifier = Modifier
                                            .size(28.dp)
                                            .padding(end = 5.dp)
                                            .align(Alignment.CenterEnd),
                                        tint = Color.White
                                    )
                                }
                            }

                        }
                    ) {
                        ListItem(
                            modifier = Modifier.clickable {},
                            headlineContent = { Text(iName.name) },
                            supportingContent = {
                                if(entry is SymptomEntry && iName is Symptom){
                                    when(iName.scale){
                                        Scale.BINARY -> Text("Occurred.")
                                        Scale.NUMERIC -> Text("Rated with ${entry.scaleValue}.")
                                        Scale.CATEGORICAL -> {
                                            val rating = when(entry.scaleValue){
                                                0 -> "bad"
                                                1 -> "medium"
                                                2 -> "good"
                                                else -> ""
                                            }
                                            Text("Rated with $rating.")
                                        }
                                    }
                                }
                            },
                            leadingContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(entry.timestamp.to24HourTime())
                                    Spacer(modifier = Modifier.width(25.dp))
                                    var imageVector = Icons.Default.SentimentSatisfied

                                    if(entry is FoodEntry){
                                        imageVector = Icons.Default.Restaurant
                                    }
                                    else if(entry is SymptomEntry && iName is Symptom){
                                        imageVector = when(iName.scale){
                                            Scale.BINARY -> Icons.Default.SentimentDissatisfied
                                            Scale.NUMERIC -> Icons.Default.Speed
                                            Scale.CATEGORICAL ->
                                                when(entry.scaleValue){
                                                    0 -> Icons.Default.SentimentDissatisfied
                                                    1 -> Icons.Default.SentimentNeutral
                                                    2 -> Icons.Default.SentimentSatisfied
                                                    else -> {Icons.Default.SentimentDissatisfied}
                                                }
                                        }
                                    }

                                    Icon(
                                        modifier = Modifier.size(30.dp),
                                        imageVector = imageVector,
                                        contentDescription = null
                                    )
                                }
                            },
                        )
                    }


                }
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
            uiState = DiaryUIState(),
            updateSelectedDate = {},
            increaseDay = {},
            decreaseDay = {},
            deleteEntry = {_, _ -> },
            undoDeletion = {},
            showNextMessage = {}
        )
    }
}
