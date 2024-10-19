package com.bennewehn.triggertrace.data

import android.content.ContentResolver
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bennewehn.triggertrace.utils.FileUtils.createCSVFileInDirectory
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

    override suspend fun getFoodById(foodId: Long): Food {
        return foodDao.getFoodById(foodId)
    }

    override suspend fun exportFoodsToDirectory(
        directoryUri: Uri,
        contentResolver: ContentResolver
    ) {
        val fileUri = createCSVFileInDirectory(contentResolver, directoryUri, "food.csv")
        fileUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.writer().use { writer ->
                    writer.append("id, name\n") // CSV Header
                    val food = foodDao.getAllFoods()
                        food.forEach { foodEntry ->
                            writer.append("${foodEntry.id}, ${foodEntry.name}\n")
                        }
                }
            }
        }
    }

    override suspend fun exportFoodInclusionsToDirectory(
        directoryUri: Uri,
        contentResolver: ContentResolver
    ) {
        val fileUri = createCSVFileInDirectory(contentResolver, directoryUri, "food_inclusions.csv")
        fileUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.writer().use { writer ->
                    writer.append("foodId, includedFoodId\n") // CSV Header
                    val food = foodDao.getAllFoodInclusions()
                    food.forEach { foodEntry ->
                        writer.append("${foodEntry.foodId}, ${foodEntry.includedFoodId}\n")
                    }
                }
            }
        }
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