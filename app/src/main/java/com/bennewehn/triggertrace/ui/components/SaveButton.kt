package com.bennewehn.triggertrace.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdateAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
){
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SystemUpdateAlt,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(id = R.string.save_to_diary))
        }
    }
}

@Preview(name = "AddButton Preview Light", showBackground = true)
@Preview(name = "AddButton Preview Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsSwitchPreview(){
    TriggerTraceTheme {
        Surface {
            SaveButton(onClick = {})
        }
    }
}
