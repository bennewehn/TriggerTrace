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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
        onFoodDeleted = viewModel::onFoodDeleted,
        showNextMessage = viewModel::showNextMessage,
        undoDeletion = viewModel::undoDeletion,
        onAddSelectedFoodClicked = viewModel::onAddSelectedFood
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

            Card(modifier = Modifier.fillMaxWidth()) {
                if(uiState.selectedFoods.isNotEmpty()){
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
                                ){
                                    Icon(
                                        modifier = Modifier.padding(4.dp),
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
                else {
                    Row(
                        modifier = Modifier.padding(12.dp)
                    ){
                        Icon(
                            imageVector = Icons.Default.IndeterminateCheckBox,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(13.dp))
                        Text(text = stringResource(id = R.string.nothing_selected))
                    }
                }
            }
            Button(
                modifier = Modifier.padding(top = 15.dp),
                enabled = uiState.selectedFoods.isNotEmpty(),
                onClick = onAddSelectedFoodClicked,
                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
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
            onAddSelectedFoodClicked = {}
        )
    }
}

@Preview(name = "Food Screen no items selected Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
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
            onAddSelectedFoodClicked = {}
        )
    }
}
