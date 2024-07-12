package com.bennewehn.triggertrace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bennewehn.triggertrace.ui.MainApp
import com.bennewehn.triggertrace.ui.theme.TriggerTraceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriggerTraceTheme {
                MainApp()
            }
        }
    }
}
