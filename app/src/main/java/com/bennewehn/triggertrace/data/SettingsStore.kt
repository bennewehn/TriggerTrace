package com.bennewehn.triggertrace.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(private val context: Context){
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    }

    private object PreferencesKeys{
        val LOG_POLLEN = booleanPreferencesKey("log_pollen")
        val LOG_TEMPERATURE = booleanPreferencesKey("log_temperature")
    }

    suspend fun setLogPollen(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.LOG_POLLEN] = value
        }
    }

    val logPollen: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LOG_POLLEN] ?: false
    }

    suspend fun setLogTemperature(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.LOG_TEMPERATURE] = value
        }
    }

    val logTemperature: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LOG_TEMPERATURE] ?: false
    }

}