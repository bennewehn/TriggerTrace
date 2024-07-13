package com.bennewehn.triggertrace.di

import android.app.Application
import androidx.room.Room
import com.bennewehn.triggertrace.data.MealRepository
import com.bennewehn.triggertrace.data.DefaultMealRepository
import com.bennewehn.triggertrace.data.TriggerTraceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTriggerTraceDatabase(app: Application): TriggerTraceDatabase{
        return Room.databaseBuilder(
            app,
            TriggerTraceDatabase::class.java,
            "triggertrace_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMealRepository(db: TriggerTraceDatabase): MealRepository {
        return DefaultMealRepository(db.mealDao)
    }

}