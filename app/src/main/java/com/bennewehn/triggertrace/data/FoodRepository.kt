package com.bennewehn.triggertrace.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    suspend fun insertFood(food: Food): Long

    suspend fun deleteFood(food: Food)

    fun searchItems(query: String): Flow<PagingData<Food>>

    suspend fun insertFoodComposition(foodComposition: FoodComposition)

    suspend fun insertFoodAndCompositions(food: Food, compositions: Set<Food>)

    suspend fun getFoodIdsWhereIncluded(foodId: Long): List<Long>

    suspend fun getFoodsByIds(foodIds: List<Long>): List<Food>
}