package com.bennewehn.triggertrace.ui.symptoms

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Scale
import com.bennewehn.triggertrace.data.Symptom
import com.bennewehn.triggertrace.ui.components.NavigateBackTopAppBar
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OneToTenRatingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    symptom: Symptom
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            NavigateBackTopAppBar(
                onBack = onBack,
                title = stringResource(id = R.string.rate_symptom),
            )
        })
    { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                contentAlignment = Alignment.Center
            ) {
                val text = buildAnnotatedString {
                    append(stringResource(id = R.string.rate_title))
                    append(" \"")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(symptom.name)
                    }
                    append("\" ")
                    append(stringResource(id = R.string.on_a_scale_from_one_to_ten))
                    append("...")
                }

                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }


            FlowRow(
                modifier = Modifier.align(Alignment.Center),
                maxItemsInEachRow = 5,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                for (i in 1..10) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(20),
                        contentPadding = PaddingValues()
                        ){
                       Text(
                           text = i.toString(),
                           fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Preview(name = "Rate Symptom 1 to 10 Scale Screen Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Rate Symptom 1 to 10 Scale Screen Preview Light")
@Composable
private fun SettingsScreenPreview() {
    TriggerTraceTheme {
        OneToTenRatingScreen(
            symptom = Symptom(name = "headache", scale = Scale.NUMERIC),
            onBack = {}
        )
    }
}
