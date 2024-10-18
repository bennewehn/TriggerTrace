package com.bennewehn.triggertrace.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    suspend fun insertFoodInclusion(foodInclusion: FoodInclusion)

    suspend fun insertFoodAndInclusions(food: Food, inclusions: Set<Food>)

    suspend fun getFoodIdsWhereIncluded(foodId: Long): List<Long>

    suspend fun getInclusions(foodId: Long): List<Long>

    suspend fun getParents(foodId: Long): List<Long>

    suspend fun getFoodsByIds(foodIds: List<Long>): List<Food>

    suspend fun deleteFood(food: Food)

    suspend fun updateFood(food: Food)

    fun searchItems(query: String): Flow<PagingData<Food>>

    suspend fun deleteInclusions(foodId: Long)

    suspend fun getFoodById(foodId: Long): Food
}