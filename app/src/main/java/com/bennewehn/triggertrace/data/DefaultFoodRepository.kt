package com.bennewehn.triggertrace.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class DefaultFoodRepository(private val foodDao: FoodDao) : FoodRepository {
    override suspend fun insertFood(food: Food) {
        foodDao.insertFood(food)
    }

    override suspend fun deleteFood(food: Food) {
        foodDao.deleteFood(food)
    }

    override suspend fun insertFoodWithComposedFoods(foodComposition: FoodComposition) {
        foodDao.insertFoodWithComposedFoods(foodComposition)
    }

    override fun searchItems(query: String): Flow<PagingData<Food>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {foodDao.searchItems(query)}
        ).flow
    }

}