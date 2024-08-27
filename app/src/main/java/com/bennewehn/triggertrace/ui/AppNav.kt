package com.bennewehn.triggertrace.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bennewehn.triggertrace.ui.diary.DiaryScreen
import com.bennewehn.triggertrace.ui.food.AddFoodScreen
import com.bennewehn.triggertrace.ui.food.FoodScreen
import com.bennewehn.triggertrace.ui.home.HomeScreen
import com.bennewehn.triggertrace.ui.settings.InfoScreen
import com.bennewehn.triggertrace.ui.settings.SettingsScreen
import com.bennewehn.triggertrace.ui.symptoms.AddSymptomScreen
import com.bennewehn.triggertrace.ui.symptoms.OneToTenRatingScreen
import com.bennewehn.triggertrace.ui.symptoms.SymptomsScreen


@Composable
fun AppNav(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen,
        modifier = Modifier.fillMaxSize()
    )
    {
        composable<Screen.HomeScreen> {
            HomeScreen(
                onNavigate = {
                    navController.navigate(it) {
                        launchSingleTop = true
                    }
                })
        }
        composable<Screen.FoodScreen> {
            FoodScreen(
                onBack = { navController.navigateUp() },
                onAddFood = {
                    navController.navigate(Screen.AddFoodScreen) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.SettingsScreen> {
            SettingsScreen(
                onBack = { navController.navigateUp() },
                onInfoClicked = {
                    navController.navigate(Screen.InfoScreen) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.SymptomsScreen> {
            SymptomsScreen(
                onBack = { navController.navigateUp() },
                onAddSymptom = {
                    navController.navigate(Screen.AddSymptomScreen) {
                        launchSingleTop = true
                    }
                },
                navigateScreen = {
                    navController.navigate(it){
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.OneToTenRatingScreen>(
            typeMap = Screen.OneToTenRatingScreen.typeMap
        ) { backStackEntry ->
            val screen = Screen.OneToTenRatingScreen.from(backStackEntry)
            OneToTenRatingScreen(
                onBack = { navController.navigateUp() },
                symptom = screen.symptom
            )
        }
        composable<Screen.AddSymptomScreen> {
            AddSymptomScreen(
                onBack = { navController.navigateUp() },
            )
        }
        composable<Screen.AddFoodScreen> {
            AddFoodScreen(
                onBack = { navController.navigateUp() },
            )
        }
        composable<Screen.DiaryScreen> {
            DiaryScreen(
                onBack = { navController.navigateUp() },
            )
        }
        composable<Screen.InfoScreen> {
            InfoScreen(
                onBack = { navController.navigateUp() },
            )
        }
    }
}