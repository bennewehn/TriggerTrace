package com.bennewehn.triggertrace.data

import com.bennewehn.triggertrace.utils.DateUtils
import java.util.Date

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

    override suspend fun getFoodEntriesForDay(day: Date): List<FoodEntry> {
        return foodEntryDao.getFoodEntriesForDay(DateUtils.getStartOfDay(day), DateUtils.getEndOfDay(day))
    }

    override suspend fun deleteFoodEntry(foodEntry: FoodEntry) {
        foodEntryDao.deleteFoodEntry(foodEntry)
    }

}