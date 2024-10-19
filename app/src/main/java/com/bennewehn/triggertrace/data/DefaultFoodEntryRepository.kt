package com.bennewehn.triggertrace.data

import android.content.ContentResolver
import android.net.Uri
import com.bennewehn.triggertrace.utils.DateUtils
import com.bennewehn.triggertrace.utils.FileUtils.createCSVFileInDirectory
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

    override suspend fun exportFoodEntriesToDirectory(
        directoryUri: Uri,
        contentResolver: ContentResolver
    ) {
        val fileUri = createCSVFileInDirectory(contentResolver, directoryUri, "food_entries.csv")
        fileUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.writer().use { writer ->
                    writer.append("id, foodId, timestamp\n") // CSV Header
                    val food = foodEntryDao.getAllEntries()
                    food.forEach { foodEntry ->
                        writer.append("${foodEntry.id}, ${foodEntry.foodId}, ${foodEntry.timestamp}\n")
                    }
                }
            }
        }
    }

}