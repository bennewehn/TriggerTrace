package com.bennewehn.triggertrace.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeTile(
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(
        ){
            Icon(Icons.Default.ThumbUp, contentDescription = "test")
        }
    }
}