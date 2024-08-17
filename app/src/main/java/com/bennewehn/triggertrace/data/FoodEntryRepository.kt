package com.bennewehn.triggertrace.data

interface FoodEntryRepository {
    suspend fun insertFoodEntry(foodEntry: FoodEntry)

    suspend fun insertFoodEntries(foodEntries: List<FoodEntry>)
}