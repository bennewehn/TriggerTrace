package com.bennewehn.triggertrace.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun DeletionConfirmationDialog(
    name: String,
    onDismiss: () -> Unit,
    onDeleteConfirmed: () -> Unit,
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
                            append(name)
                        }
                        append(". ")
                        append(stringResource(R.string.backup_msg))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text(stringResource(R.string.type_delete_confirm_msg)) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDeleteConfirmed()
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
private fun Preview() {
    TriggerTraceTheme {
        DeletionConfirmationDialog(
            onDismiss = {},
            onDeleteConfirmed = {},
            name = "test",
        )
    }
}
