package com.bennewehn.triggertrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bennewehn.triggertrace.ui.Routes
import com.bennewehn.triggertrace.ui.home.HomeScreen
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriggerTraceTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.HomeScreen) {
                    composable<Routes.HomeScreen>{
                        HomeScreen()
                    }
                }
            }
        }
    }
}

