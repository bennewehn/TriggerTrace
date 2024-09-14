package com.bennewehn.triggertrace.ui.symptoms

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun BinaryRatingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    symptom: Symptom,
    onValueSelected: (Boolean) -> Unit
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val text = buildAnnotatedString {
                    append(stringResource(id = R.string.did_symptom))
                    append(" \"")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(symptom.name)
                    }
                    append("\" ")
                    append(stringResource(id = R.string.occur))
                    append("?")
                }

                Spacer(Modifier.height(30.dp))

                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(30.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10),
                    onClick = { onValueSelected(true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF43A047),
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = null
                    )
                }

                Spacer(Modifier.height(15.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10),
                    onClick = { onValueSelected(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336),
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Default.ThumbDown, contentDescription = null
                    )
                }
            }

        }
    }
}


@Preview(
    name = "Binary Rating Screen Preview Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "Binary Rating Screen Preview Light")
@Composable
private fun BinaryRatingScreenPreview() {
    TriggerTraceTheme {
        BinaryRatingScreen(
            symptom = Symptom(name = "headache", scale = Scale.BINARY),
            onBack = {},
            onValueSelected = {}
        )
    }
}
