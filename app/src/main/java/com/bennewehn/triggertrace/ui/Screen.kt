package com.bennewehn.triggertrace.ui

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen{
    @Serializable
    data object HomeScreen : Screen()
    @Serializable
    data object FoodScreen : Screen()
    @Serializable
    data object SymptomsScreen: Screen()
    @Serializable
    data object AddSymptomScreen: Screen()
    @Serializable
    data object SettingsScreen: Screen()
    @Serializable
    data object AddFoodScreen: Screen()
    @Serializable
    data object InfoScreen: Screen()
    @Serializable
    data object DiaryScreen: Screen()
}