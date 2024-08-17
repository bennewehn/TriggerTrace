package com.bennewehn.triggertrace.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bennewehn.triggertrace.data.DefaultFoodEntryRepository
import com.bennewehn.triggertrace.data.DefaultFoodRepository
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.data.SettingsStore
import com.bennewehn.triggertrace.data.TriggerTraceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
             "trigger_trace_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFoodRepository(db: TriggerTraceDatabase): FoodRepository {
        return DefaultFoodRepository(db.foodDao)
    }

    @Provides
    @Singleton
    fun provideFoodEntryRepository(db: TriggerTraceDatabase): FoodEntryRepository{
        return DefaultFoodEntryRepository(db.foodEntryDao)
    }

    @Provides
    @Singleton
    fun provideSettingsStore(@ApplicationContext appContext: Context): SettingsStore {
        return SettingsStore(appContext)
    }

}