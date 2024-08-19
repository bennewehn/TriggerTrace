package com.bennewehn.triggertrace.ui.components

import android.content.res.Configuration
import android.text.format.DateFormat
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import java.util.Date

@Composable
fun DateInputField(
    openDatePicker: () -> Unit,
    selectedDate: Date
){
    val dateFormat: java.text.DateFormat = DateFormat.getDateFormat(LocalContext.current)
    val formattedDate = dateFormat.format(selectedDate)

    OutlinedTextField(
        readOnly = true,
        value = formattedDate,
        onValueChange = {},
        label = { Text(text = stringResource(id = R.string.date)) },
        trailingIcon = {
            Icon(imageVector = Icons.Default.EditCalendar, contentDescription = null)
        },
        // works like onClick
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            openDatePicker()
                        }
                    }
                }
            }
    )
}

@Preview(name = "DateInputField Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "DateInputField Preview Light")
@Composable
fun DateInputFieldPreview(){
    TriggerTraceTheme {
        Surface{
            DateInputField(openDatePicker = { }, selectedDate = Date())
        }
    }
}