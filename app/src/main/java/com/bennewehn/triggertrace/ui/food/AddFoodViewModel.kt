package com.bennewehn.triggertrace.ui.food

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.ui.components.FoodSearchBarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddFoodUIState(
    val name: String = "",
    val userMessage: Int? = null,
    val selectedFoods: Set<Food> = emptySet(),
    val foodAddedSuccessfully: Boolean = false
)

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    foodEntryRepository: FoodEntryRepository,
    private val foodRepository: FoodRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFoodUIState())
    val uiState: StateFlow<AddFoodUIState> = _uiState.asStateFlow()

    val foodSearchBarViewModel = FoodSearchBarViewModel(foodEntryRepository, foodRepository)

    fun addFood() {
        // Check if name is empty
        if (uiState.value.name.isEmpty()) {
            _uiState.update {
                it.copy(userMessage = R.string.name_cannot_be_empty_message)
            }
            return
        }
        // Add Food to db
        viewModelScope.launch {
            try {
                val food = Food(name = uiState.value.name)
                foodRepository.insertFoodAndCompositions(food, uiState.value.selectedFoods)
                _uiState.update {
                    it.copy(foodAddedSuccessfully = true)
                }
            } catch (_: SQLiteConstraintException) {
                _uiState.update {
                    it.copy(userMessage = R.string.name_already_used_message)
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(userMessage = R.string.error_message_adding_food)
                }
            }
        }
    }

    fun deselectFood(food: Food) {
        _uiState.update {
            it.copy(
                selectedFoods = it.selectedFoods - food,
            )
        }
    }

    fun selectFood(food: Food) {
        _uiState.update {
            it.copy(
                selectedFoods = it.selectedFoods + food,
            )
        }
        foodSearchBarViewModel.updateSearchBarActive(false)
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    fun updateName(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

}