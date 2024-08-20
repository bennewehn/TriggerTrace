package com.bennewehn.triggertrace.ui.components

import android.content.res.Configuration
import android.text.format.DateFormat
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import java.util.Calendar
import java.util.Date

@Composable
fun TimeInputField(
    modifier: Modifier = Modifier,
    openTimePicker: () -> Unit,
    selectedTime: Date
){

    val time = DateFormat.getTimeFormat(LocalContext.current).format(selectedTime)

    OutlinedTextField(
        modifier = modifier,
        readOnly = true,
        value = time,
        onValueChange = {},
        label = { Text(text = stringResource(id = R.string.time)) },
        trailingIcon = {
            Icon(imageVector = Icons.Default.AccessTimeFilled, contentDescription = null)
        },
        // works like onClick
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            openTimePicker()
                        }
                    }
                }
            }
    )
}

@Preview(name = "TimeInputField Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "TimeInputField Preview Light")
@Composable
fun TimeInputFieldPreview(){
    TriggerTraceTheme {
        Surface{
            TimeInputField(openTimePicker = {}, selectedTime = Date())
        }
    }
}
