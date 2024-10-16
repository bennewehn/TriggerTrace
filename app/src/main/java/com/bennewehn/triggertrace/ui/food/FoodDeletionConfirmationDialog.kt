package com.bennewehn.triggertrace.ui.food

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.ui.components.DeletionConfirmationDialog
import com.bennewehn.triggertrace.ui.components.FoodSearchBarViewModel

@Composable
fun FoodDeletionConfirmationDialog(
    foodSearchBarViewModel: FoodSearchBarViewModel
) {

    val deleteDialogState by foodSearchBarViewModel.deletionDialogState.collectAsStateWithLifecycle()

    DeletionConfirmationDialog(
        name = deleteDialogState.food?.name.toString(),
        onDismiss = foodSearchBarViewModel::dismissDeletionConfirmationDialog,
        onDeleteConfirmed = { deleteDialogState.food?.let { foodSearchBarViewModel.deleteFoodWithDiaryEntries(it) } },
    )

}
