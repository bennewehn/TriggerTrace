package com.bennewehn.triggertrace.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
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
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    placeHolder: String = "Placeholder",
    colors: SearchBarColors = SearchBarDefaults.colors(),
    onFoodSelected: (Food) -> Unit,
    viewModel: FoodSearchBarViewModel?
) {

    val uiState = viewModel?.uiState?.collectAsStateWithLifecycle()?.value ?: FoodSearchBarState()

    MyFoodSearchBar(
        modifier = modifier,
        leadingIcon = leadingIcon,
        placeHolder = placeHolder,
        colors = colors,
        updateSearchQuery = { query -> viewModel?.updateSearchQuery(query) },
        updateSearchBarActive = { active -> viewModel?.updateSearchBarActive(active) },
        onFoodSelected = onFoodSelected,
        foodSearchBarState = uiState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyFoodSearchBar(
    modifier: Modifier,
    leadingIcon: ImageVector,
    placeHolder: String,
    updateSearchQuery: (String) -> Unit,
    updateSearchBarActive: (Boolean) -> Unit,
    onFoodSelected: (Food) -> Unit,
    colors: SearchBarColors,
    foodSearchBarState: FoodSearchBarState
) {

    val foodPagedData: LazyPagingItems<Food>? =
        foodSearchBarState.foodPagedData?.collectAsLazyPagingItems()

    SearchBar(
        modifier = modifier,
        query = foodSearchBarState.searchQuery,
        onQueryChange = updateSearchQuery,
        onSearch = {},
        colors = colors,
        active = foodSearchBarState.searchBarActive,
        onActiveChange = updateSearchBarActive,
        windowInsets = WindowInsets(top = 0.dp),
        placeholder = { Text(text = placeHolder) },
        leadingIcon = { Icon(leadingIcon, null) },
        trailingIcon = {
            if (foodSearchBarState.searchBarActive) {
                Icon(
                    modifier = Modifier.clickable {
                        if (foodSearchBarState.searchQuery.isNotEmpty()) {
                            updateSearchQuery("")
                        } else {
                            updateSearchBarActive(false)
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
    ) {
        foodPagedData?.let { lazyPagingItems ->
            LazyColumn{
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
            leadingIcon = Icons.Default.Search,
            placeHolder = "Search food",
            onFoodSelected = {},
            viewModel = null
        )
    }
}

