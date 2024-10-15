package com.bennewehn.triggertrace.ui.food

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bennewehn.triggertrace.ui.components.FoodDeletionDialogState
import com.bennewehn.triggertrace.ui.components.FoodSearchBarViewModel
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun FoodDeletionConfirmationDialog(
    foodSearchBarViewModel: FoodSearchBarViewModel
) {

    val deleteDialogState by foodSearchBarViewModel.deletionDialogState.collectAsStateWithLifecycle()

    Content(
        onDismiss = foodSearchBarViewModel::dismissDeletionConfirmationDialog,
        onDeleteConfirmed = foodSearchBarViewModel::deleteFoodWithDiaryEntries,
        foodDeletionDialogState = deleteDialogState
    )

}

@Composable
private fun Content(
    onDismiss: () -> Unit,
    onDeleteConfirmed: (Food) -> Unit,
    foodDeletionDialogState: FoodDeletionDialogState
) {
    var inputText by remember { mutableStateOf("") }

    // Dialog for deletion confirmation
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(R.string.confirm_deletion)) },
        text = {
            Column {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.type_in_delete_confirmation_msg))
                        append(" ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(foodDeletionDialogState.food?.name)
                        }
                        append(". ")
                        append(stringResource(R.string.backup_msg))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Type 'delete' to confirm") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    foodDeletionDialogState.food?.let { onDeleteConfirmed(it) }
                    onDismiss() // Close the dialog after deletion
                },
                enabled = inputText == "delete" // Only enable when input is correct
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(name = "Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Preview Light")
@Composable
private fun DiaryConflictPreview() {
    TriggerTraceTheme {
        Content(
            onDismiss = {},
            onDeleteConfirmed = {},
            foodDeletionDialogState = FoodDeletionDialogState()
        )
    }
}

