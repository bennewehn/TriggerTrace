package com.bennewehn.triggertrace.ui

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.toRoute
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.data.Symptom
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.collections.mapOf
import kotlin.reflect.typeOf

@Serializable
sealed class Screen {
    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object FoodScreen : Screen()

    @Serializable
    data object SymptomsScreen : Screen()

    @Serializable
    data object AddSymptomScreen : Screen()

    @Serializable
    data object SettingsScreen : Screen()

    @Serializable
    data object AddFoodScreen : Screen()

    @Serializable
    data object InfoScreen : Screen()

    @Serializable
    data object DiaryScreen : Screen()

    @Serializable
    data class EditFoodScreen(
        val food: Food,
        val parentIds: List<Long>,
        val children: List<Food>
    ) : Screen() {
        companion object {
            val typeMap = mapOf(
                typeOf<Food>() to serializableType<Food>(),
                typeOf<List<Long>>() to serializableType<List<Long>>(),
                typeOf<List<Food>>() to serializableType<List<Food>>()
            )

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<EditFoodScreen>(typeMap)

            fun from(backStackEntry: NavBackStackEntry) =
                backStackEntry.toRoute<EditFoodScreen>()
        }
    }

    @Serializable
    data class OneToTenRatingScreen(val symptom: Symptom) : Screen() {
        companion object {
            val typeMap = mapOf(typeOf<Symptom>() to serializableType<Symptom>())

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<OneToTenRatingScreen>(typeMap)

            fun from(backStackEntry: NavBackStackEntry) =
                backStackEntry.toRoute<OneToTenRatingScreen>()
        }
    }

    @Serializable
    data class BinaryRatingScreen(val symptom: Symptom) : Screen() {
        companion object {
            val typeMap = mapOf(typeOf<Symptom>() to serializableType<Symptom>())

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<BinaryRatingScreen>(typeMap)

            fun from(backStackEntry: NavBackStackEntry) =
                backStackEntry.toRoute<BinaryRatingScreen>()
        }
    }

    @Serializable
    data class CategoricalRatingScreen(val symptom: Symptom) : Screen() {
        companion object {
            val typeMap = mapOf(typeOf<Symptom>() to serializableType<Symptom>())

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<CategoricalRatingScreen>(typeMap)

            fun from(backStackEntry: NavBackStackEntry) =
                backStackEntry.toRoute<CategoricalRatingScreen>()
        }
    }

    @Serializable
    data class SaveSymptomEntryScreen(val symptom: Symptom, val value: Int) : Screen() {
        companion object {
            val typeMap = mapOf(
                typeOf<Symptom>() to serializableType<Symptom>(),
            )

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<SaveSymptomEntryScreen>(typeMap)

            fun from(backStackEntry: NavBackStackEntry) =
                backStackEntry.toRoute<SaveSymptomEntryScreen>()
        }
    }

    @Serializable
    data class SaveFoodEntryScreen(val foods: List<Food>) : Screen() {
        companion object {
            val typeMap = mapOf(
                typeOf<List<Food>>() to serializableType<List<Food>>(),
            )

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<SaveFoodEntryScreen>(typeMap)

            fun from(backStackEntry: NavBackStackEntry) =
                backStackEntry.toRoute<SaveFoodEntryScreen>()
        }
    }

}

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}
