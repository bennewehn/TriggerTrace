package com.bennewehn.triggertrace.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class DefaultFoodRepository(private val foodDao: FoodDao) : FoodRepository {
    override suspend fun insertFood(food: Food): Long {
        return foodDao.insertFood(food)
    }

    override suspend fun deleteFood(food: Food) {
        foodDao.deleteFood(food)
    }

    override fun searchItems(query: String): Flow<PagingData<Food>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { foodDao.searchItems(query) }
        ).flow
    }

    override suspend fun insertFoodComposition(foodComposition: FoodComposition) {
        foodDao.insertFoodComposition(foodComposition)
    }

    override suspend fun insertFoodAndCompositions(food: Food, compositions: Set<Food>) {
        foodDao.insertFoodAndCompositions(food, compositions)
    }

    override suspend fun getFoodIdsWhereIncluded(foodId: Long): List<Long> {
        return foodDao.getFoodIdsWhereIncluded(foodId)
    }

    override suspend fun getFoodsByIds(foodIds: List<Long>): List<Food> {
        return foodDao.getFoodsByIds(foodIds)
    }

}