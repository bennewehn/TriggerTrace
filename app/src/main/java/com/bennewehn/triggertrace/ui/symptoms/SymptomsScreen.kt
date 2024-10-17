package com.bennewehn.triggertrace.ui.symptoms

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.bennewehn.triggertrace.data.Scale
import com.bennewehn.triggertrace.data.Symptom
import com.bennewehn.triggertrace.ui.Screen
import com.bennewehn.triggertrace.ui.components.AppDefaultSearchBar
import com.bennewehn.triggertrace.ui.components.DeletionConfirmationDialog
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun SymptomsScreen(
    onBack: () -> Unit,
    onAddSymptom: () -> Unit,
    navigateScreen: (Screen) -> Unit,
    viewModel: SymptomsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deleteDialogState by viewModel.deletionState.collectAsStateWithLifecycle()

    SymptomsScreenContent(
        onBack = onBack,
        onAddSymptom = onAddSymptom,
        state = uiState,
        onSymptomSelected = { symptom: Symptom ->
            when (symptom.scale) {
                Scale.NUMERIC -> navigateScreen(Screen.OneToTenRatingScreen(symptom))
                Scale.CATEGORICAL -> navigateScreen(Screen.CategoricalRatingScreen(symptom))
                Scale.BINARY -> navigateScreen(Screen.BinaryRatingScreen(symptom))
            }
        },
        onSearchQueryUpdated = viewModel::updateSearchQuery,
        openDeletionDialog = viewModel::openDeletionDialog,
        deletionDialogState = deleteDialogState,
        dismissDeletionDialog = viewModel::dismissDeletionDialog,
        onConfirmDeletion = { deleteDialogState.symptom?.let { viewModel.deleteSymptom(it) } },
        openDeletionConfirmationDialog = viewModel::openDeletionConfirmationDialog,
        dismissDeletionConfirmDialog = viewModel::dismissDeletionConfirmationDialog,
        deleteWithEntries = { deleteDialogState.symptom?.let { viewModel.deleteSymptomWithEntries(it) } },
        openEditingScreen = { symptom -> navigateScreen(Screen.EditSymptomScreen(symptom)) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SymptomsScreenContent(
    onBack: () -> Unit,
    onAddSymptom: () -> Unit,
    onSymptomSelected: (Symptom) -> Unit,
    onSearchQueryUpdated: (String) -> Unit,
    openDeletionDialog: (Symptom) -> Unit,
    openEditingScreen: (Symptom) -> Unit,
    dismissDeletionDialog: () -> Unit,
    dismissDeletionConfirmDialog: () -> Unit,
    onConfirmDeletion: () -> Unit,
    deleteWithEntries: () -> Unit,
    openDeletionConfirmationDialog: () -> Unit,
    state: SymptomsScreenState,
    deletionDialogState: SymptomsDeletionState
) {
    val symptomPagedData: LazyPagingItems<Symptom>? =
        state.symptomPagedData?.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            NavigateBackTopAppBar(
                title = stringResource(id = R.string.symptom_screen_title),
                onBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddSymptom) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_food_btn))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            if (deletionDialogState.showConfirmationDialog) {
                DeletionConfirmationDialog(
                    name = deletionDialogState.symptom?.name.toString(),
                    onDismiss = dismissDeletionConfirmDialog,
                    onDeleteConfirmed = deleteWithEntries,
                )
            }

            if (deletionDialogState.showDialog) {
                SymptomDeletionDialog(
                    onDismiss = dismissDeletionDialog,
                    onConfirm = onConfirmDeletion,
                    onDeleteWithDiaryEntries = openDeletionConfirmationDialog,
                    dialogState = deletionDialogState,
                )
            }

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

                                val dismissState = rememberSwipeToDismissBoxState()

                                LaunchedEffect(dismissState.currentValue) {
                                    when (dismissState.currentValue) {
                                        SwipeToDismissBoxValue.EndToStart -> {
                                            openDeletionDialog(symptom)
                                        }
                                        SwipeToDismissBoxValue.StartToEnd -> {
                                            openEditingScreen(symptom)
                                        }
                                        SwipeToDismissBoxValue.Settled -> {}
                                    }

                                    if(dismissState.currentValue != SwipeToDismissBoxValue.Settled){
                                        dismissState.reset()
                                    }

                                }

                                SwipeToDismissBox(
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
                                            }
                                            else if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) {
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
                                        modifier = Modifier.clickable { onSymptomSelected(symptom) },
                                        headlineContent = { Text(symptom.name) },
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

            BackHandler(true) {
                onBack()
            }

        }
    }
}

@Preview(name = "Symptoms Screen Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Symptoms Screen Preview Light")
@Composable
private fun SymptomsScreenPreview() {
    TriggerTraceTheme {
        SymptomsScreenContent(
            onBack = {},
            onAddSymptom = {},
            state = SymptomsScreenState(),
            onSymptomSelected = {},
            onSearchQueryUpdated = {},
            openDeletionDialog = {},
            deletionDialogState = SymptomsDeletionState(),
            dismissDeletionDialog = {},
            onConfirmDeletion = {},
            openDeletionConfirmationDialog = {},
            dismissDeletionConfirmDialog = {},
            deleteWithEntries = {},
            openEditingScreen = {}
        )
    }
}