package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bennewehn.triggertrace.R
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
    FoodScreenContent(
        modifier = modifier,
        onBack = onBack,
        onAddFood = onAddFood,
        foodSearchBarViewModel = viewModel.foodSearchBarViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodScreenContent(
    modifier: Modifier,
    onBack: () -> Unit,
    onAddFood: () -> Unit,
    foodSearchBarViewModel: FoodSearchBarViewModel?
) {
    Scaffold(
        modifier = modifier,
        topBar = { FoodTopAppBar(onBack) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddFood) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_food_btn))
            }
        }
    ) { innerPadding ->
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
                onFoodSelected = {},
                viewModel = foodSearchBarViewModel
            )
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
            foodSearchBarViewModel = null
        )
    }
}