package com.bennewehn.triggertrace.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.data.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val settingsStore: SettingsStore
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

}