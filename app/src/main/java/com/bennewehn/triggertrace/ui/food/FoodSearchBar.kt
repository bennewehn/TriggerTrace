package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.ui.components.AppDefaultSearchBar
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import kotlin.Long

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchBar(
    modifier: Modifier = Modifier,
    onFoodSelected: (Food) -> Unit,
    leadingIcon: ImageVector = Icons.Default.Search,
    excludeFoodIds: List<Long> = emptyList(),
    placeHolder: String = "Search",
    enableSwipeToDelete: Boolean = false,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    viewModel: FoodSearchBarViewModel?
) {

    val uiState = viewModel?.uiState?.collectAsStateWithLifecycle()?.value ?: FoodSearchBarState()
    val deletionDialogState = viewModel?.deletionDialogState?.collectAsStateWithLifecycle()?.value
        ?: FoodDeletionDialogState()

    val editState = viewModel?.editFoodState?.collectAsStateWithLifecycle()?.value ?: EditFoodDialogState()

    MyFoodSearchBar(
        modifier = modifier,
        updateSearchQuery = { query -> viewModel?.updateSearchQuery(query) },
        leadingIcon = leadingIcon,
        placeHolder = placeHolder,
        colors = colors,
        onFoodSelected = onFoodSelected,
        foodSearchBarState = uiState,
        deletionDialogState = deletionDialogState,
        excludeFoodIds = excludeFoodIds,
        searchBarActiveChanged = { active -> viewModel?.updateSearchBarActive(active) },
        deleteFood = { food -> viewModel?.openDeletionDialog(food) },
        enableSwipeToDelete = enableSwipeToDelete,
        editState = editState,
        editFood = { food -> viewModel?.editFood(food) },
        dismissEditScreen = { viewModel?.dismissEditDialog() }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyFoodSearchBar(
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    placeHolder: String,
    colors: SearchBarColors,
    updateSearchQuery: (String) -> Unit,
    onFoodSelected: (Food) -> Unit,
    deleteFood: (Food) -> Unit,
    editFood: (Food) -> Unit,
    enableSwipeToDelete: Boolean,
    searchBarActiveChanged: (Boolean) -> Unit,
    excludeFoodIds: List<Long> = emptyList(),
    dismissEditScreen: () -> Unit,
    foodSearchBarState: FoodSearchBarState,
    editState: EditFoodDialogState,
    deletionDialogState: FoodDeletionDialogState
) {

    val foodPagedData: LazyPagingItems<Food>? =
        foodSearchBarState.foodPagedData?.collectAsLazyPagingItems()

    AppDefaultSearchBar(
        modifier = modifier,
        onSearchQueryChanged = updateSearchQuery,
        leadingIcon = leadingIcon,
        placeHolder = placeHolder,
        searchBarActive = foodSearchBarState.searchBarActive,
        searchBarActiveChanged = searchBarActiveChanged,
        colors = colors
    ) {
        foodPagedData?.let { lazyPagingItems ->
            LazyColumn {
                items(lazyPagingItems.itemCount) { index ->
                    val food = lazyPagingItems[index]

                    food?.let {

                        val dismissState = rememberSwipeToDismissBoxState()

                        LaunchedEffect(deletionDialogState.showDialog) {
                            if (deletionDialogState.showDialog) {
                                dismissState.reset()
                            }
                        }

                        LaunchedEffect(dismissState.currentValue) {
                            when (dismissState.currentValue) {
                                SwipeToDismissBoxValue.EndToStart -> deleteFood(food)
                                SwipeToDismissBoxValue.StartToEnd -> {
                                    if (!editState.showEditScreen) {
                                        editFood(food)
                                    }
                                }
                                SwipeToDismissBoxValue.Settled -> {}
                            }
                        }

                        // disable swiping functions for item int list
                        if (it.id !in excludeFoodIds) {
                            SwipeToDismissBox(
                                gesturesEnabled = enableSwipeToDelete,
                                state = dismissState,
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
                                        } else if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit",
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .padding(start = 10.dp)
                                                    .align(Alignment.CenterStart),
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }) {
                                ListItem(
                                    modifier = Modifier.clickable { onFoodSelected(food) },
                                    headlineContent = { Text(food.name) },
                                )
                            }
                        } else {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        food.name + " (" + stringResource(R.string.disabled) + ")",
                                        color = Color.Gray
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        stringResource(R.string.includes_this_food),
                                        color = Color.Gray
                                    )
                                },
                            )
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Preview Light")
@Composable
private fun FoodSearchBarPreview() {
    TriggerTraceTheme {
        FoodSearchBar(
            onFoodSelected = {},
            viewModel = null,
        )
    }
}

