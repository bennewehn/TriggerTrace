package com.bennewehn.triggertrace.ui

import android.os.Bundle
import androidx.annotation.StringRes
import com.bennewehn.triggertrace.R
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Serializable
sealed class Screen(@StringRes val title: Int){
    @Serializable
    data object HomeScreen : Screen(title = R.string.home_screen_title)
    @Serializable
    data object FoodScreen : Screen(title = R.string.food_screen_title)
    @Serializable
    data object SymptomsScreen: Screen(title = R.string.symptom_screen_title)
    @Serializable
    data object SettingsScreen: Screen(title = R.string.settings_screen_title)

    companion object {
        private fun fromRoute(route: String, args: Bundle?): Screen? {
            val subclass = Screen::class.sealedSubclasses.firstOrNull {
                route.contains(it.qualifiedName.toString())
            }
            return subclass?.let { createInstance(it, args) }
        }

        private fun <T : Any> createInstance(kClass: KClass<T>, bundle: Bundle?): T? {
            val constructor = kClass.primaryConstructor
            return constructor?.let {
                val args = it.parameters.associateWith { param ->
                    bundle?.getBundle(param.name)
                }
                it.callBy(args)
            } ?: kClass.objectInstance
        }

        fun fromNavBackStackEntry(backStackEntry: androidx.navigation.NavBackStackEntry?): Screen? {
           return fromRoute(
               route = backStackEntry?.destination?.route ?: "",
               args = backStackEntry?.arguments
           )
        }
    }
}