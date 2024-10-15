package com.bennewehn.triggertrace.data

class DefaultFoodEntryRepository(private val foodEntryDao: FoodEntryDao) : FoodEntryRepository {

    override suspend fun insertFoodEntry(foodEntry: FoodEntry) {
        foodEntryDao.insertFoodEntry(foodEntry)
    }

    override suspend fun insertFoodEntries(foodEntries: List<FoodEntry>) {
        foodEntryDao.insertFoodEntries(foodEntries = foodEntries)
    }

    override suspend fun foodEntryCount(id: Long): Long {
        return foodEntryDao.foodEntryCount(id)
    }

    override suspend fun deleteAllByFoodId(foodId: Long) {
        foodEntryDao.deleteAllByFoodId(foodId)
    }

}