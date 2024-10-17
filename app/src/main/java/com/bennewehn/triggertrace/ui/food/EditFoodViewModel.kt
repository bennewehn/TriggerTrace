package com.bennewehn.triggertrace.ui.food

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.data.FoodInclusion
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditFoodUIState(
    val name: String = "",
    val food: Food = Food(name = ""),
    val userMessage: Int? = null,
    val selectedFoods: Set<Food> = emptySet(),
    val foodUpdatedSuccessfully: Boolean = false,
    val parentIds: List<Long> = emptyList()
)

@HiltViewModel
class EditFoodViewModel @Inject constructor(
    foodEntryRepository: FoodEntryRepository,
    private val foodRepository: FoodRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val food = Screen.EditFoodScreen.from(savedStateHandle).food
    val parents = Screen.EditFoodScreen.from(savedStateHandle).parentIds
    val children = Screen.EditFoodScreen.from(savedStateHandle).children

    private val _uiState = MutableStateFlow(
        EditFoodUIState(
            name = food.name,
            food = food,
            selectedFoods = children.toSet(),
            parentIds = parents
        )
    )
    val uiState: StateFlow<EditFoodUIState> = _uiState.asStateFlow()

    val foodSearchBarViewModel = FoodSearchBarViewModel(foodEntryRepository, foodRepository)

    fun editFood() {
        // Check if name is empty
        if (uiState.value.name.isEmpty()) {
            _uiState.update {
                it.copy(userMessage = R.string.name_cannot_be_empty_message)
            }
            return
        }
        // update db
        viewModelScope.launch {
            // update food
            val updatedFood = food.copy(name = _uiState.value.name)
            foodRepository.updateFood(updatedFood)

            // update inclusions
            // 1. remove old inclusions
            foodRepository.deleteInclusions(food.id)
            // 2. insert new inclusions
            _uiState.value.selectedFoods.forEach { e ->
                foodRepository.insertFoodInclusion(
                    FoodInclusion(
                        foodId = food.id,
                        includedFoodId = e.id
                    )
                )
            }

            _uiState.update {
                it.copy(foodUpdatedSuccessfully = true)
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
