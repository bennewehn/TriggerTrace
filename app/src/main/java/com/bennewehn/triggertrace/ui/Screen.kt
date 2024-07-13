package com.bennewehn.triggertrace.ui

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(){
    @Serializable
    data object HomeScreen : Screen()
    @Serializable
    data object FoodScreen : Screen()
    @Serializable
    data object SymptomsScreen: Screen()
    @Serializable
    data object SettingsScreen: Screen()
}