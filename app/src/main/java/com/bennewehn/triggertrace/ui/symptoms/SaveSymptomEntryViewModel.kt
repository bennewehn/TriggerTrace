package com.bennewehn.triggertrace.ui.symptoms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.data.SymptomEntry
import com.bennewehn.triggertrace.data.SymptomEntryRepository
import com.bennewehn.triggertrace.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


data class SaveSymptomEntryState(
    val selectedDate: Date = Date(),
    val showSuccessfulDialog: Boolean = false,
    val navigateHome: Boolean = false
)

@HiltViewModel
class SaveSymptomEntryViewModel @Inject constructor(
    private val symptomEntryRepository: SymptomEntryRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val symptom = Screen.SaveSymptomEntryScreen.from(savedStateHandle).symptom
    val value = Screen.SaveSymptomEntryScreen.from(savedStateHandle).value

    private val _uiState = MutableStateFlow(SaveSymptomEntryState())
    val uiState: StateFlow<SaveSymptomEntryState> = _uiState.asStateFlow()

    fun saveEntry(){
        val entry = SymptomEntry(
            timestamp = _uiState.value.selectedDate,
            symptomId = symptom.id,
            scaleValue = value
        )
        viewModelScope.launch {
            symptomEntryRepository.insertSymptomEntry(entry)
            _uiState.update {
                it.copy(
                    showSuccessfulDialog = true
                )
            }
        }
    }

    fun onSuccessDialogDismissed(){
        _uiState.update {
            it.copy(
                showSuccessfulDialog = false,
                navigateHome = true
            )
        }
    }


    fun updateSelectedDate(date: Date) {
        _uiState.update {
            it.copy(
                selectedDate = date
            )
        }
    }

    fun updateHour(hour: Int){
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.selectedDate
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        _uiState.update {
            it.copy(
                selectedDate = calendar.time
            )
        }
    }

    fun updateMinute(minute: Int){
        val calendar = Calendar.getInstance()
        calendar.time = _uiState.value.selectedDate
        calendar.set(Calendar.MINUTE, minute)
        _uiState.update {
            it.copy(
                selectedDate = calendar.time
            )
        }
    }

}