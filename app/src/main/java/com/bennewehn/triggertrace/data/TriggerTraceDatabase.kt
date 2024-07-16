package com.bennewehn.triggertrace.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Food::class, FoodComposition::class], version = 1)
abstract class TriggerTraceDatabase: RoomDatabase() {
    abstract val foodDao: FoodDao
}