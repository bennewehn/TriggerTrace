package com.bennewehn.triggertrace.data

class DefaultMealRepository(private val mealDao: MealDao) : MealRepository {
    override suspend fun insertMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    override suspend fun deleteMeal(meal: Meal) {
        mealDao.deleteMeal(meal)
    }

    override suspend fun getMealById(id: Int): Meal? {
        return mealDao.getMealById(id)
    }
}