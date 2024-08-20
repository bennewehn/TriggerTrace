package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.ui.components.DateInputField
import com.bennewehn.triggertrace.ui.components.DatePickerModal
import com.bennewehn.triggertrace.ui.components.FoodSearchBar
import com.bennewehn.triggertrace.ui.components.FoodSearchBarViewModel
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.components.TimeInputField
import com.bennewehn.triggertrace.ui.components.TimePickerDialog
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import java.util.Date

@Composable
fun FoodScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onAddFood: () -> Unit,
    viewModel: FoodViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FoodScreenContent(
        modifier = modifier,
        onBack = onBack,
        onAddFood = onAddFood,
        foodSearchBarViewModel = viewModel.foodSearchBarViewModel,
        uiState = uiState,
        onFoodSelected = viewModel::onFoodSelected,
        onFoodDeleted = viewModel::onFoodDeleted,
        showNextMessage = viewModel::showNextMessage,
        undoDeletion = viewModel::undoDeletion,
        onAddSelectedFoodClicked = viewModel::onAddSelectedFood,
        onDismissSuccessfulDialog = viewModel::onDismissSuccessfulDialog,
        updateDate = viewModel::updateSelectedDate,
        updateHour = viewModel::updateHour,
        updateMinute = viewModel::updateMinute
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodScreenContent(
    modifier: Modifier,
    onBack: () -> Unit,
    onAddFood: () -> Unit,
    onAddSelectedFoodClicked: () -> Unit,
    onFoodSelected: (Food) -> Unit,
    onFoodDeleted: (Food) -> Unit,
    foodSearchBarViewModel: FoodSearchBarViewModel?,
    showNextMessage: () -> Unit,
    undoDeletion: (Food) -> Unit,
    uiState: FoodScreenUIState,
    onDismissSuccessfulDialog: () -> Unit,
    updateDate: (Date) -> Unit,
    updateHour: (Int) -> Unit,
    updateMinute: (Int) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val undoLabel = stringResource(id = R.string.UNDO)
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (uiState.showSuccessfulDialog) {
        FoodAddedDialog(onDismissSuccessfulDialog)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.food_screen_title)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddFood) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_food_btn))
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

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

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { dateMillis ->
                    dateMillis?.let {
                        updateDate(Date(it))
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 25.dp)
                .clip(RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DateInputField(
                openDatePicker = { showDatePicker = true},
                selectedDate = uiState.selectedDate
            )

            Spacer(modifier = Modifier.height(5.dp))

            TimeInputField(
                openTimePicker = { showTimePicker = true },
                selectedTime = uiState.selectedDate
            )

            Spacer(modifier = Modifier.height(20.dp))

            FoodSearchBar(
                leadingIcon = Icons.Default.Search,
                placeHolder = stringResource(id = R.string.search_food),
                onFoodSelected = onFoodSelected,
                viewModel = foodSearchBarViewModel
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                if (uiState.selectedFoods.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        items(uiState.selectedFoods) { food ->
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = food.name)
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { onFoodDeleted(food) },
                                ) {
                                    Icon(
                                        modifier = Modifier.padding(4.dp),
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.IndeterminateCheckBox,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(13.dp))
                        Text(text = stringResource(id = R.string.nothing_selected))
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                enabled = uiState.selectedFoods.isNotEmpty(),
                onClick = onAddSelectedFoodClicked,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LibraryAdd,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Add")
                }
            }
        }
    }
}

@Composable
private fun FoodAddedDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.saved_successfully),
                    style = TextStyle(
                        fontSize = 23.sp,
                    ),
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(text = "OK")
                }
            }
        }
    }
}

@Preview(name = "Food Screen Items selected Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Food Screen Items selected Preview Light")
@Composable
private fun FoodScreenItemsSelectedPreview() {
    TriggerTraceTheme {
        FoodScreenContent(
            modifier = Modifier,
            onBack = {},
            onAddFood = {},
            foodSearchBarViewModel = null,
            uiState = FoodScreenUIState(
                selectedFoods = listOf(
                    Food(name = "Milk"),
                    Food(name = "Chocolate"),
                    Food(name = "Water"),
                )
            ),
            onFoodSelected = {},
            showNextMessage = {},
            undoDeletion = {},
            onFoodDeleted = {},
            onAddSelectedFoodClicked = {},
            onDismissSuccessfulDialog = {},
            updateDate = {},
            updateMinute = {},
            updateHour = {}
        )
    }
}

@Preview(
    name = "Food Screen no items selected Preview Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "Food Screen no items selected Preview Light")
@Composable
private fun FoodScreenNoItemsSelectedPreview() {
    TriggerTraceTheme {
        FoodScreenContent(
            modifier = Modifier,
            onBack = {},
            onAddFood = {},
            foodSearchBarViewModel = null,
            uiState = FoodScreenUIState(),
            onFoodSelected = {},
            showNextMessage = {},
            undoDeletion = {},
            onFoodDeleted = {},
            onAddSelectedFoodClicked = {},
            onDismissSuccessfulDialog = {},
            updateDate = {},
            updateHour = {},
            updateMinute = {}
        )
    }
}

@Preview(name = "Food added dialog dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Food added dialog light")
@Composable
private fun FoodAddedDialogPreview() {
    TriggerTraceTheme {
        FoodAddedDialog(onDismiss = {})
    }
}
