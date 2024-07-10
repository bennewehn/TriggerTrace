package com.bennewehn.triggertrace.data

interface MealRepository {
    suspend fun insertMeal(meal: Meal)

    suspend fun deleteMeal(meal: Meal)

    suspend fun getMealById(id: Int): Meal?
}