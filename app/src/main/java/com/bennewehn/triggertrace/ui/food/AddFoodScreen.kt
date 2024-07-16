package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddFoodScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onAddFood: () -> Unit,
    viewModel: AddFoodViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = { FoodTopAppBar(onBack) },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val foodPagedData: LazyPagingItems<Food>? =
            uiState.foodPagedData?.collectAsLazyPagingItems()
        val focusRequester = remember { FocusRequester() }
        var hasFocused by rememberSaveable { mutableStateOf(false) }


        LaunchedEffect(hasFocused) {
            if (!hasFocused) {
                focusRequester.requestFocus()
                hasFocused = true
            }
        }

        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(id = userMessage)
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(
                    message = snackbarText,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
                viewModel.snackbarMessageShown()
            }
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = uiState.name,
                    onValueChange = viewModel::updateName,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.add_food_name_hint),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 1f)
                    ),
                )

                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::updateSearchQuery,
                    onSearch = {},
                    active = uiState.searchBarActive,
                    onActiveChange = viewModel::updateSearchBarActive,
                    placeholder = { Text(text = stringResource(id = R.string.add_foods)) },
                    leadingIcon = { Icon(Icons.Filled.Add, null) },
                    windowInsets = WindowInsets(top = 0.dp),
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    trailingIcon = {
                        if (uiState.searchBarActive) {
                            Icon(
                                modifier = Modifier.clickable {
                                    if (uiState.searchQuery.isNotEmpty()) {
                                        viewModel.updateSearchQuery("")
                                    } else {
                                        viewModel.updateSearchBarActive(false)
                                    }
                                },
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }

                ) {
                    foodPagedData?.let { lazyPagingItems ->
                        LazyColumn {
                            items(lazyPagingItems.itemCount) { index ->
                                val food = lazyPagingItems[index]
                                food?.let {
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.selectFood(food) }
                                        .padding(14.dp)
                                    ) {
                                        Text(text = food.name)
                                    }
                                }
                            }

                            when (lazyPagingItems.loadState.refresh) {
                                is LoadState.Loading -> {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(20.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }

                                is LoadState.Error -> {
                                    val e = lazyPagingItems.loadState.refresh as LoadState.Error
                                    item { Text(text = "Error: ${e.error.localizedMessage}") }
                                }

                                is LoadState.NotLoading -> {}
                            }
                        }
                    }
                }

                FlowRow(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.selectedFoods.forEach { item ->
                        InputChip(
                            selected = true,
                            onClick = { viewModel.deselectFood(item) },
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
                onClick = viewModel::addFood,
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.add_food_save))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.add_food_screen_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.back_button))
            }
        }
    )
}

@Preview(name = "Add Food Screen Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Add Food Screen Preview Light")
@Composable
private fun FoodScreenPreview() {
    TriggerTraceTheme {
        AddFoodScreen(onBack = {}, onAddFood = {})
    }
}
