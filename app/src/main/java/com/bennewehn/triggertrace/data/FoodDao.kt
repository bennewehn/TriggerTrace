package com.bennewehn.triggertrace.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFood(food: Food)

    @Insert
    suspend fun insertFoodWithComposedFoods(foodComposition: FoodComposition)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM food WHERE name LIKE '%' || :query || '%'")
    fun searchItems(query: String): PagingSource<Int, Food>


}