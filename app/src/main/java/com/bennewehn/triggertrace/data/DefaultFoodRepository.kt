package com.bennewehn.triggertrace.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class DefaultFoodRepository(private val foodDao: FoodDao) : FoodRepository {

    override suspend fun deleteFood(food: Food) {
        foodDao.deleteFood(food)
    }

    override fun searchItems(query: String): Flow<PagingData<Food>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { foodDao.searchItems(query) }
        ).flow
    }

    override suspend fun deleteInclusions(foodId: Long) {
        foodDao.deleteInclusions(foodId)
    }

    override suspend fun insertFoodInclusion(foodInclusion: FoodInclusion) {
        foodDao.insertFoodInclusion(foodInclusion)
    }

    override suspend fun insertFoodAndInclusions(
        food: Food,
        inclusions: Set<Food>
    ) {
        foodDao.insertFoodAndInclusions(food, inclusions)
    }

    override suspend fun getFoodIdsWhereIncluded(foodId: Long): List<Long> {
        return foodDao.getFoodIdsWhereIncluded(foodId)
    }

    override suspend fun getInclusions(foodId: Long): List<Long> {
        return foodDao.getInclusions(foodId)
    }

    override suspend fun getFoodsByIds(foodIds: List<Long>): List<Food> {
        return foodDao.getFoodsByIds(foodIds)
    }

    override suspend fun getParents(foodId: Long): List<Long> {
        return foodDao.getParents(foodId)
    }

    override suspend fun updateFood(food: Food) {
        foodDao.updateFood(food)
    }

}