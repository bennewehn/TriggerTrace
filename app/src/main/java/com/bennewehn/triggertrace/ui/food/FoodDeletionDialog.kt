package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun FoodDeletionDialog(
    foodSearchBarViewModel: FoodSearchBarViewModel
) {

    val deleteDialogState by foodSearchBarViewModel.deletionDialogState.collectAsStateWithLifecycle()

    FoodDeletionDialogContent(
        onDismiss = foodSearchBarViewModel::dismissDeletionDialog,
        onConfirm = { deleteDialogState.food?.let { foodSearchBarViewModel.deleteFood(it) } },
        onDeleteWithDiaryEntries = {
            // open confirmation dialog
            foodSearchBarViewModel.openDeletionConfirmationDialog()
        },
        dialogState = deleteDialogState
    )
}

@Composable
private fun FoodDeletionDialogContent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDeleteWithDiaryEntries: () -> Unit,
    dialogState: FoodDeletionDialogState
) {
    var title = stringResource(R.string.confirm_deletion)
    if (dialogState.conflictFoods.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.conflicting_items)) },
            text = {
                Column {
                    Text(
                        buildAnnotatedString {
                            append(stringResource(R.string.conflicting_items_error_message1) + " ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(dialogState.food?.name)
                            }
                            append(" " + stringResource(R.string.conflicting_items_error_message2))
                        })

                    dialogState.conflictFoods.forEach { food ->
                        Text(
                            buildAnnotatedString {
                                append("- ") // Regular prefix
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(food.name)
                                }
                            }
                        )
                    }

                }
            },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                }) {
                    Text("OK")
                }
            }
        )
    } else if (dialogState.foodExitsInDiary) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.conflicting_diary_entries)) },
            text = {
                Column {
                    Text(
                        buildAnnotatedString {
                            append(stringResource(R.string.food_deletion_conflict_diary_message1) + " ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(dialogState.food?.name)
                            }
                            append(". " + stringResource(R.string.food_deletion_conflict_diary_message2))
                        })
                }
            },
            confirmButton = {
                Button(onClick = {
                    dialogState.food?.let {
                        onDeleteWithDiaryEntries()
                    }
                    onDismiss()
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = {
                Column {
                    Text(stringResource(R.string.food_deletion_msg))
                }
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Preview(name = "Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Preview Light")
@Composable
private fun Preview() {
    TriggerTraceTheme {
        FoodDeletionDialogContent(
            onConfirm = {},
            onDismiss = {},
            onDeleteWithDiaryEntries = {},
            dialogState = FoodDeletionDialogState(
                showDialog = true,
                food = Food(1, "bread"),
                conflictFoods = emptyList(),
                foodExitsInDiary = false
            ),
        )
    }
}


@Preview(name = "Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Preview Light")
@Composable
private fun ConflictPreview() {
    TriggerTraceTheme {
        FoodDeletionDialogContent(
            onConfirm = {},
            onDismiss = {},
            onDeleteWithDiaryEntries = {},
            dialogState = FoodDeletionDialogState(
                showDialog = true,
                food = Food(1, "bread"),
                foodExitsInDiary = false,
                conflictFoods = listOf(Food(0, "apple"))
            ),
        )
    }
}


@Preview(name = "Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Preview Light")
@Composable
private fun DiaryConflictPreview() {
    TriggerTraceTheme {
        FoodDeletionDialogContent(
            onConfirm = {},
            onDismiss = {},
            onDeleteWithDiaryEntries = {},
            dialogState = FoodDeletionDialogState(
                showDialog = true,
                food = Food(1, "bread"),
                foodExitsInDiary = true,
                conflictFoods = emptyList()
            ),
        )
    }
}

