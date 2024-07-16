package com.bennewehn.triggertrace.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    suspend fun insertFood(food: Food)

    suspend fun deleteFood(food: Food)

    suspend fun insertFoodWithComposedFoods(foodComposition: FoodComposition)

    fun searchItems(query: String): Flow<PagingData<Food>>
}