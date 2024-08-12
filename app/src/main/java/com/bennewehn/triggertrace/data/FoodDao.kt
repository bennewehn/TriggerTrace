package com.bennewehn.triggertrace.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFood(food: Food): Long

    @Insert
    suspend fun insertFoodComposition(foodComposition: FoodComposition)

    @Transaction
    suspend fun insertFoodAndCompositions(food: Food, compositions: Set<Food>) {
        val foodId = insertFood(food)
        compositions.forEach {
            insertFoodComposition(FoodComposition(foodId, it.id))
        }
    }

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM food WHERE name LIKE '%' || :query || '%'")
    fun searchItems(query: String): PagingSource<Int, Food>


}