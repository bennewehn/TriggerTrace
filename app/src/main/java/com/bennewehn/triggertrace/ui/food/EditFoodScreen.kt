package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.ui.components.NameInputField
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun EditFoodScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: EditFoodViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.foodUpdatedSuccessfully) {
        if (uiState.foodUpdatedSuccessfully) {
            onBack()
        }
    }

    Content(
        modifier = modifier,
        onBack = onBack,
        snackbarMessageShown = viewModel::snackbarMessageShown,
        updateName = viewModel::updateName,
        selectFood = viewModel::selectFood,
        deselectFood = viewModel::deselectFood,
        onEditFood = viewModel::editFood,
        uiState = uiState,
        foodSearchBarViewModel = viewModel.foodSearchBarViewModel
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onEditFood: () -> Unit,
    snackbarMessageShown: () -> Unit,
    updateName: (String) -> Unit,
    selectFood: (Food) -> Unit,
    deselectFood: (Food) -> Unit,
    uiState: EditFoodUIState,
    foodSearchBarViewModel: FoodSearchBarViewModel? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.edit_food_title),
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
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                NameInputField(
                    name = uiState.name,
                    onNameChanged = updateName,
                    shouldFocus = false
                )

                FoodSearchBar(
                    leadingIcon = Icons.Filled.Add,
                    placeHolder = stringResource(id = R.string.include_foods),
                    onFoodSelected = selectFood,
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    disableFoodIds = uiState.parentIds,
                    // exclude all selected foods and the currently edited food from the search
                    hideFoodIds = uiState.selectedFoods.map { it.id } + uiState.food.id,
                    viewModel = foodSearchBarViewModel,
                )


                FlowRow(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.selectedFoods.forEach { item ->
                        InputChip(
                            selected = true,
                            onClick = { deselectFood(item) },
                            label = { Text(text = item.name) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(30.dp),
                onClick = onEditFood,
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
private fun FoodScreenPreview() {
    TriggerTraceTheme {
        Content(
            onBack = {},
            onEditFood = {},
            updateName = {},
            deselectFood = {},
            selectFood = {},
            snackbarMessageShown = {},
            uiState = EditFoodUIState(
                selectedFoods = setOf(
                    Food(name = "Wheat"),
                    Food(name = "Banana"),
                    Food(name = "Apple"),
                    Food(name = "Strawberry")
                ),
            ),
        )
    }
}

@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "Light")
@Composable
private fun FoodScreenSearchBarActivePreview() {
    TriggerTraceTheme {
        Content(
            onBack = {},
            onEditFood = {},
            updateName = {},
            deselectFood = {},
            selectFood = {},
            snackbarMessageShown = {},
            uiState = EditFoodUIState(
            ),
        )
    }
}
