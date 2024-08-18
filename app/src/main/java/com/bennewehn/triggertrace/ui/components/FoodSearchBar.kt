package com.bennewehn.triggertrace.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchBar(
    onFoodSelected: (Food) -> Unit,
    leadingIcon: ImageVector = Icons.Default.Search,
    placeHolder: String = "Search",
    colors: SearchBarColors = SearchBarDefaults.colors(),
    viewModel: FoodSearchBarViewModel?
) {

    val uiState = viewModel?.uiState?.collectAsStateWithLifecycle()?.value ?: FoodSearchBarState()

    MyFoodSearchBar(
        updateSearchQuery = { query -> viewModel?.updateSearchQuery(query) },
        leadingIcon = leadingIcon,
        placeHolder = placeHolder,
        colors = colors,
        onFoodSelected = onFoodSelected,
        foodSearchBarState = uiState,
        searchBarActiveChanged = {active -> viewModel?.updateSearchBarActive(active)}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyFoodSearchBar(
    leadingIcon: ImageVector,
    placeHolder: String,
    colors: SearchBarColors,
    updateSearchQuery: (String) -> Unit,
    onFoodSelected: (Food) -> Unit,
    searchBarActiveChanged: (Boolean) -> Unit,
    foodSearchBarState: FoodSearchBarState
) {

    val foodPagedData: LazyPagingItems<Food>? =
        foodSearchBarState.foodPagedData?.collectAsLazyPagingItems()

    AppDefaultSearchBar(
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
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFoodSelected(food) }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Food Search Bar Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Food Search Bar Preview Light")
@Composable
private fun FoodSearchBarPreview() {
    TriggerTraceTheme {
        FoodSearchBar(
            onFoodSelected = {},
            viewModel = null
        )
    }
}

