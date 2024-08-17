package com.bennewehn.triggertrace.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface FoodEntryDao {
    @Insert
    suspend fun insertFoodEntry(foodEntry: FoodEntry)

    @Insert
    suspend fun insertFoodEntries(foodEntries: List<FoodEntry>)
}