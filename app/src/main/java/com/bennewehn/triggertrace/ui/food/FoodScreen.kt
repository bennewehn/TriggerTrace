package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.ui.components.FoodSearchBar
import com.bennewehn.triggertrace.ui.components.FoodSearchBarViewModel
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

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
        onFoodItemSelectionChanged = viewModel::onFoodItemSelectionChanged,
        showNextMessage = viewModel::showNextMessage,
        undoDeletion = viewModel::undoDeletion
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodScreenContent(
    modifier: Modifier,
    onBack: () -> Unit,
    onAddFood: () -> Unit,
    onFoodSelected: (Food) -> Unit,
    onFoodItemSelectionChanged: (Boolean, SelectableFood) -> Unit,
    foodSearchBarViewModel: FoodSearchBarViewModel?,
    showNextMessage: () -> Unit,
    undoDeletion: (Food) -> Unit,
    uiState: FoodScreenUIState
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val undoLabel = stringResource(id = R.string.UNDO)


    Scaffold(
        modifier = modifier,
        topBar = { FoodTopAppBar(onBack) },
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

                if(result == SnackbarResult.ActionPerformed){
                    undoDeletion(uiState.messageQueue.first().second)
                }
                showNextMessage()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 25.dp)
                .clip(RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FoodSearchBar(
                leadingIcon = Icons.Default.Search,
                placeHolder = stringResource(id = R.string.search_food),
                onFoodSelected = onFoodSelected,
                viewModel = foodSearchBarViewModel
            )

            Spacer(modifier = Modifier.height(50.dp))

            Card {
                LazyColumn {
                    items(uiState.selectedFoods) { selectableFood ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectableFood.selected,
                                onCheckedChange = { checked ->
                                    onFoodItemSelectionChanged(
                                        checked,
                                        selectableFood
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = selectableFood.food.name)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.food_screen_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.back_button))
            }
        }
    )
}

@Preview(name = "Food Screen Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Food Screen Preview Light")
@Composable
private fun FoodScreenPreview() {
    TriggerTraceTheme {
        FoodScreenContent(
            modifier = Modifier,
            onBack = {},
            onAddFood = {},
            foodSearchBarViewModel = null,
            uiState = FoodScreenUIState(
                selectedFoods = listOf(
                    SelectableFood(Food(name = "Milk"), selected = true),
                    SelectableFood(Food(name = "Chocolate"), selected = false),
                    SelectableFood(Food(name = "Water"), selected = true),
                )
            ),
            onFoodSelected = {},
            onFoodItemSelectionChanged = { _, _ -> },
            showNextMessage = {},
            undoDeletion = {}
        )
    }
}