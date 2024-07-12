package com.bennewehn.triggertrace.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bennewehn.triggertrace.ui.Screen


@Composable
fun HomeScreen(
    onNavigate: (Screen) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {

        HomeCard(
            icon = { modifier ->
                Icon(
                    modifier = modifier
                        .background(
                            MaterialTheme.colorScheme.onPrimary,
                            shape = CircleShape
                        )
                        .padding(8.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.Restaurant,
                    contentDescription = "Food Icon",
                )
            },
            text = "Food",
            onClick = { onNavigate(Screen.FoodScreen) }
        )
        HomeCard(
            icon = { modifier ->
                Icon(
                    modifier = modifier,
                    imageVector = Icons.Filled.SentimentDissatisfied,
                    contentDescription = "Symptoms Icon",
                )
            },
            text = "Symptoms",
            onClick = { onNavigate(Screen.SymptomsScreen) }
        )

        HomeCard(
            icon = { modifier ->
                Icon(
                    modifier = modifier,
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings Icon",
                )
            },
            text = "Settings",
            onClick = { onNavigate(Screen.SettingsScreen) }
        )
    }
}

@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    text: String,
    icon: @Composable (modifier: Modifier) -> Unit,
    onClick: (() -> Unit)? = null
) {
    ElevatedCard(
        onClick = { onClick?.invoke() },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            icon(Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
