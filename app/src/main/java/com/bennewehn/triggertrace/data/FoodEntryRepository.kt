package com.bennewehn.triggertrace.data

import java.util.Date

interface FoodEntryRepository {
    suspend fun insertFoodEntry(foodEntry: FoodEntry)

    suspend fun insertFoodEntries(foodEntries: List<FoodEntry>)

    suspend fun foodEntryCount(id: Long): Long

    suspend fun deleteAllByFoodId(foodId: Long)

    suspend fun getFoodEntriesForDay(day: Date): List<FoodEntry>

    suspend fun deleteFoodEntry(foodEntry: FoodEntry)
}