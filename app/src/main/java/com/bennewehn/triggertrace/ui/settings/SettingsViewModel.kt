package com.bennewehn.triggertrace.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.data.SettingsStore
import com.bennewehn.triggertrace.data.SymptomEntryRepository
import com.bennewehn.triggertrace.data.SymptomRepository
import com.bennewehn.triggertrace.data.TriggerTraceDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SettingsUIState(
    val logPollen: Boolean = true,
    val logTemperature: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStore: SettingsStore,
    private val db: TriggerTraceDatabase,
    private val foodRepository: FoodRepository,
    private val foodEntryRepository: FoodEntryRepository,
    private val symptomsRepository: SymptomRepository,
    private val symptomEntryRepository: SymptomEntryRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(settingsStore.logTemperature, settingsStore.logPollen) { logTemp, logPollen ->
                SettingsUIState(
                    logTemperature = logTemp,
                    logPollen = logPollen
                )
            }
                .collect { newState ->
                    _uiState.update { newState }
                }

        }
    }

    fun updateLogPollen(logPollen: Boolean) {
        _uiState.update {
            it.copy(logPollen = logPollen)
        }
        viewModelScope.launch {
            settingsStore.setLogPollen(logPollen)
        }
    }

    fun updateLogTemperature(logTemp: Boolean) {
        _uiState.update {
            it.copy(logTemperature = logTemp)
        }
        viewModelScope.launch {
            settingsStore.setLogTemperature(logTemp)
        }
    }

    fun exportDatabase(uri: Uri){
        viewModelScope.launch {
            db.backupDatabase(context, uri)
        }
    }

    fun exportCsvFilesToDirectory(directoryUri: Uri) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            foodRepository.exportFoodsToDirectory(directoryUri, contentResolver)
            foodRepository.exportFoodInclusionsToDirectory(directoryUri, contentResolver)
            foodEntryRepository.exportFoodEntriesToDirectory(directoryUri, contentResolver)
            symptomsRepository.exportSymptomsToDirectory(directoryUri, contentResolver)
            symptomEntryRepository.exportSymptomEntriesToDirectory(directoryUri, contentResolver)
        }
    }

}