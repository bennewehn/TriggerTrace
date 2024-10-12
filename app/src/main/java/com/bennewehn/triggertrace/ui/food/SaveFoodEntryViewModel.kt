package com.bennewehn.triggertrace.ui.food

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.data.FoodEntry
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


data class SaveFoodEntryUIState(
    val showSuccessfulDialog: Boolean = false,
    val selectedDate: Date = Date(),
    val navigateHome: Boolean = false
)

@HiltViewModel
class SaveFoodEntryViewModel @Inject constructor(
    private val foodEntryRepository: FoodEntryRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext val appContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaveFoodEntryUIState())
    val uiState: StateFlow<SaveFoodEntryUIState> = _uiState.asStateFlow()

    val foods = Screen.SaveFoodEntryScreen.from(savedStateHandle).foods

    fun onAddSelectedFood(){
        // add selected food items to db
        viewModelScope.launch {
            val entries = foods.map { FoodEntry(foodId = it.id, timestamp = _uiState.value.selectedDate) }
            try{
                foodEntryRepository.insertFoodEntries(entries)
                _uiState.update {
                    it.copy(
                        showSuccessfulDialog = true,
                    )
                }
            }
            catch (e: Exception){
                Toast.makeText(appContext, e.message, Toast.LENGTH_SHORT).show()
            }
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

    fun onDismissSuccessfulDialog(){
        _uiState.update {
            it.copy(
                showSuccessfulDialog = false,
                navigateHome = true
            )
        }
    }

}