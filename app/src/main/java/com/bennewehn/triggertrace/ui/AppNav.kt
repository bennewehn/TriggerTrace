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
import com.bennewehn.triggertrace.ui.food.EditFoodScreen
import com.bennewehn.triggertrace.ui.food.FoodScreen
import com.bennewehn.triggertrace.ui.food.SaveFoodEntryScreen
import com.bennewehn.triggertrace.ui.home.HomeScreen
import com.bennewehn.triggertrace.ui.settings.InfoScreen
import com.bennewehn.triggertrace.ui.settings.SettingsScreen
import com.bennewehn.triggertrace.ui.symptoms.AddSymptomScreen
import com.bennewehn.triggertrace.ui.symptoms.OneToTenRatingScreen
import com.bennewehn.triggertrace.ui.symptoms.BinaryRatingScreen
import com.bennewehn.triggertrace.ui.symptoms.CategoricalRatingScreen
import com.bennewehn.triggertrace.ui.symptoms.SaveSymptomEntryScreen
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
                },
                onAddSelectedFoodsClicked = { foods ->
                    navController.navigate(Screen.SaveFoodEntryScreen(foods)){
                        launchSingleTop = true
                    }
                },
                onNavigateEditScreen = { food, parentIds, children ->
                    navController.navigate(Screen.EditFoodScreen(food, parentIds, children))
                }
            )
        }
        composable<Screen.SaveFoodEntryScreen>(
            typeMap = Screen.SaveFoodEntryScreen.typeMap
        ) { backStackEntry ->
            SaveFoodEntryScreen(
                onBack = { navController.navigateUp() },
                onNavigateHome = { navController.popBackStack(Screen.HomeScreen, false) }
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
                symptom = screen.symptom,
                onValueSelected = { value: Int ->
                    navController.navigate(Screen.SaveSymptomEntryScreen(screen.symptom, value)){
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.BinaryRatingScreen>(
            typeMap = Screen.BinaryRatingScreen.typeMap
        ) { backStackEntry ->
            val screen = Screen.BinaryRatingScreen.from(backStackEntry)
            BinaryRatingScreen(
                onBack = { navController.navigateUp() },
                symptom = screen.symptom,
                onValueSelected = { value: Boolean ->
                    navController.navigate(Screen.SaveSymptomEntryScreen(screen.symptom, value = if (value) 0 else 1)){
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.CategoricalRatingScreen>(
            typeMap = Screen.CategoricalRatingScreen.typeMap
        ) { backStackEntry ->
            val screen = Screen.CategoricalRatingScreen.from(backStackEntry)
            CategoricalRatingScreen(
                onBack = { navController.navigateUp() },
                symptom = screen.symptom,
                onValueSelected = { value: Int ->
                    navController.navigate(Screen.SaveSymptomEntryScreen(screen.symptom, value = value)){
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Screen.SaveSymptomEntryScreen>(
            typeMap = Screen.SaveSymptomEntryScreen.typeMap
        ) {
            SaveSymptomEntryScreen(
                onBack = { navController.navigateUp() },
                onNavigateHome = { navController.popBackStack(Screen.HomeScreen, false) }
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
        composable<Screen.EditFoodScreen>(
            typeMap = Screen.EditFoodScreen.typeMap
        ) {
            EditFoodScreen(
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