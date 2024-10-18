package com.bennewehn.triggertrace.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFood(food: Food): Long

    @Insert
    suspend fun insertFoodInclusion(foodInclusion: FoodInclusion)

    @Transaction
    suspend fun insertFoodAndInclusions(food: Food, inclusions: Set<Food>) {
        val foodId = insertFood(food)
        inclusions.forEach {
            insertFoodInclusion(FoodInclusion(foodId, it.id))
        }
    }

    @Query("SELECT foodId from food_inclusions where includedFoodId= :foodId")
    suspend fun getFoodIdsWhereIncluded(foodId: Long): List<Long>

    @Query("SELECT includedFoodId from food_inclusions where foodId = :foodId")
    suspend fun getInclusions(foodId: Long): List<Long>

    @Query("DELETE from food_inclusions where foodId = :foodId")
    suspend fun deleteInclusions(foodId: Long)

    @Query("SELECT foodId from food_inclusions where includedFoodId = :foodId")
    suspend fun getParents(foodId: Long): List<Long>

    @Query("SELECT * from food where id IN (:foodIds)")
    suspend fun getFoodsByIds(foodIds: List<Long>): List<Food>

    @Delete
    suspend fun deleteFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Query("SELECT * FROM food WHERE name LIKE '%' || :query || '%'")
    fun searchItems(query: String): PagingSource<Int, Food>

    @Query("SELECT * FROM food WHERE id = :foodId")
    suspend fun getFoodById(foodId: Long): Food


}