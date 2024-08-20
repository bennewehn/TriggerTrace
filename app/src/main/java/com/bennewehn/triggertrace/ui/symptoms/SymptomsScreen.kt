package com.bennewehn.triggertrace.ui.symptoms

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.data.Symptom
import com.bennewehn.triggertrace.ui.components.AppDefaultSearchBar
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun SymptomsScreen(
onBack: () -> Unit,
onAddSymptom: () -> Unit,
viewModel: SymptomsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SymptomsScreenContent(
        onBack = onBack,
        onAddSymptom = onAddSymptom,
        state = uiState,
        onSymptomSelected = viewModel::onSymptomSelected,
        onSearchQueryUpdated = viewModel::updateSearchQuery
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SymptomsScreenContent(
    onBack: () -> Unit,
    onAddSymptom: () -> Unit,
    onSymptomSelected: (Symptom) -> Unit,
    onSearchQueryUpdated: (String) -> Unit,
    state: SymptomsScreenState
){
    val symptomPagedData: LazyPagingItems<Symptom>? =
        state.symptomPagedData?.collectAsLazyPagingItems()

    Scaffold(
        topBar = { SymptomsTopAppBar(onBack) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddSymptom) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_food_btn))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){
            AppDefaultSearchBar(
                searchBarActive = true,
                searchBarActiveChanged = {},
                onSearchQueryChanged = onSearchQueryUpdated,
                colors = SearchBarDefaults.colors(
                    containerColor = Color.Transparent
                )
            ) {
                symptomPagedData?.let { lazyPagingItems ->
                    LazyColumn {
                        items(lazyPagingItems.itemCount) { index ->
                            val symptom = lazyPagingItems[index]
                            symptom?.let {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSymptomSelected(symptom) }
                                    .padding(14.dp)
                                ) {
                                    Text(text = symptom.name)
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SymptomsTopAppBar(onBack: () -> Unit){
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.symptom_screen_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(id = R.string.back_button))
            }
        },
    )
}

@Preview(name = "Symptoms Screen Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Symptoms Screen Preview Light")
@Composable
private fun SymptomsScreenPreview(){
    TriggerTraceTheme {
        SymptomsScreenContent(
            onBack = {},
            onAddSymptom = {},
            state = SymptomsScreenState(),
            onSymptomSelected = {},
            onSearchQueryUpdated = {}
        )
    }
}