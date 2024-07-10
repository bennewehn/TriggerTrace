package com.bennewehn.triggertrace.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Meal::class], version = 1, exportSchema = false)

abstract class TriggerTraceDatabase: RoomDatabase() {
    abstract val mealDao: MealDao
}