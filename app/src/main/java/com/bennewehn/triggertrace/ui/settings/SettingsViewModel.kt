package com.bennewehn.triggertrace.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class SettingsUIState(
    val logPollen: Boolean = true,
    val logTemperature: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    fun updateLogPollen(logPollen: Boolean) {
        _uiState.update {
            it.copy(logPollen = logPollen)
        }
    }

    fun updateLogTemperature(logTemp: Boolean) {
        _uiState.update {
            it.copy(logTemperature = logTemp)
        }
    }

}