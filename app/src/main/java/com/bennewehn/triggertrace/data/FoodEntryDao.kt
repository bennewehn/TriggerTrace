package com.bennewehn.triggertrace.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodEntryDao {
    @Insert
    suspend fun insertFoodEntry(foodEntry: FoodEntry)

    @Query("SELECT COUNT(*) FROM food_entries WHERE foodId = :id")
    suspend fun foodEntryCount(id: Long): Long

    @Insert
    suspend fun insertFoodEntries(foodEntries: List<FoodEntry>)

    @Query("DELETE FROM food_entries WHERE foodId = :foodId")
    suspend fun deleteAllByFoodId(foodId: Long)

    @Query("SELECT * FROM food_entries WHERE timestamp BETWEEN :startOfDay AND :endOfDay")
    suspend fun getFoodEntriesForDay(startOfDay: Long, endOfDay: Long): List<FoodEntry>
}