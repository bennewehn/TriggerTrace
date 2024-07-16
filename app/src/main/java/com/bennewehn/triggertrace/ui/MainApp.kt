package com.bennewehn.triggertrace.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bennewehn.triggertrace.ui.food.AddFoodScreen
import com.bennewehn.triggertrace.ui.food.FoodScreen
import com.bennewehn.triggertrace.ui.home.HomeScreen
import com.bennewehn.triggertrace.ui.settings.InfoScreen
import com.bennewehn.triggertrace.ui.settings.SettingsScreen
import com.bennewehn.triggertrace.ui.symptoms.SymptomsScreen


@Composable
fun MainApp(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen,
        modifier = Modifier
            .fillMaxSize()
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
                onAddFood = { navController.navigate(Screen.AddFoodScreen){
                    launchSingleTop = true
                } }
            )
        }
        composable<Screen.SettingsScreen> {
            SettingsScreen(
                onBack = { navController.navigateUp() },
                onInfoClicked = { navController.navigate(Screen.InfoScreen){
                    launchSingleTop = true
                } }
            )
        }
        composable<Screen.SymptomsScreen> {
            SymptomsScreen(
                onBack = { navController.navigateUp() },
            )
        }
        composable<Screen.AddFoodScreen> {
            AddFoodScreen(
                onBack = { navController.navigateUp() },
                onAddFood = {}
            )
        }
        composable<Screen.InfoScreen> {
            InfoScreen(
                onBack = { navController.navigateUp() },
            )
        }
    }
}