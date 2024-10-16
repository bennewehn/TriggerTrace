package com.bennewehn.triggertrace.ui.symptoms

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Scale
import com.bennewehn.triggertrace.data.Symptom
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun SymptomDeletionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDeleteWithDiaryEntries: () -> Unit,
    dialogState: SymptomsDeletionState
) {

    if (dialogState.hasConflicts) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.conflicting_items)) },
            text = {
                Text(
                    buildAnnotatedString {
                        append(stringResource(R.string.delete_symptom_conflicts_msg1) + " ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(dialogState.symptom?.name)
                        }
                        append(". " + stringResource(R.string.delete_symptom_conflicts_msg2))
                    })
            },
            confirmButton = {
                Button(onClick = {
                    onDeleteWithDiaryEntries()
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
            title = { Text(stringResource(R.string.confirm_deletion)) },
            text = {
                Column {
                    Text(stringResource(R.string.symptom_deletion_msg))
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
        SymptomDeletionDialog(
            onDismiss = {},
            onConfirm = {},
            onDeleteWithDiaryEntries = {},
            dialogState = SymptomsDeletionState(
                symptom = Symptom(name = "Test", scale = Scale.CATEGORICAL)
            ),
        )
    }
}

@Preview(name = "Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Preview Light")
@Composable
private fun PreviewConflict() {
    TriggerTraceTheme {
        SymptomDeletionDialog(
            onDismiss = {},
            onConfirm = {},
            onDeleteWithDiaryEntries = {},
            dialogState = SymptomsDeletionState(
                symptom = Symptom(name = "Test", scale = Scale.CATEGORICAL),
                hasConflicts = true
            ),
        )
    }
}

