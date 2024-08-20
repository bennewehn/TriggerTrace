package com.bennewehn.triggertrace.ui.food

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.data.FoodEntry
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.ui.components.FoodSearchBarViewModel
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

data class FoodScreenUIState(
    val selectedFoods: List<Food> = emptyList(),
    val messageQueue: List<Pair<String, Food>> = emptyList(),
    val showSuccessfulDialog: Boolean = false,
    val selectedDate: Date = Date()
)

@HiltViewModel
class FoodViewModel @Inject constructor(
    foodRepository: FoodRepository,
    private val foodEntryRepository: FoodEntryRepository,
    @ApplicationContext val appContext: Context
) : ViewModel() {

    val foodSearchBarViewModel = FoodSearchBarViewModel(foodRepository)

    private val _uiState = MutableStateFlow(FoodScreenUIState())
    val uiState: StateFlow<FoodScreenUIState> = _uiState.asStateFlow()

    fun onAddSelectedFood(){
        // add selected food items to db
        viewModelScope.launch {
            val entries = _uiState.value.selectedFoods.map { FoodEntry(foodId = it.id, timestamp = _uiState.value.selectedDate) }
            try{
                foodEntryRepository.insertFoodEntries(entries)
                _uiState.update {
                    it.copy(
                        showSuccessfulDialog = true,
                        selectedFoods = emptyList()
                    )
                }
            }
            catch (e: Exception){
                Toast.makeText(appContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addFood(food: Food) {
        val containsFood = _uiState.value.selectedFoods.any { it == food }
        if (!containsFood) {
            _uiState.update {
                it.copy(
                    selectedFoods = it.selectedFoods + food
                )
            }
        }
    }

    fun onFoodSelected(food: Food) {
        foodSearchBarViewModel.updateSearchBarActive(false)
        addFood(food)
    }

    fun onFoodDeleted(food: Food) {
        _uiState.update {
            it.copy(
                selectedFoods = _uiState.value.selectedFoods.filter { item ->
                    item != food
                }
            )
        }
        enqueueMessage(
            "${food.name} ${appContext.getString(R.string.deleted)}.",
            food
        )
    }

    private fun enqueueMessage(message: String, food: Food) {
        // only trigger state change if not items in queue
        _uiState.update {
            it.copy(messageQueue = it.messageQueue + Pair(message, food))
        }
    }

    fun showNextMessage() {
        viewModelScope.launch {
            if (_uiState.value.messageQueue.isNotEmpty()) {
                // Remove the first message after it's shown
                _uiState.update {
                    it.copy(
                        messageQueue = it.messageQueue.drop(1)
                    )
                }
            }
        }
    }

    fun onDismissSuccessfulDialog(){
        _uiState.update {
            it.copy(
                showSuccessfulDialog = false
            )
        }
    }

    fun undoDeletion(food: Food) {
        addFood(food)
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