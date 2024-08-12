package com.bennewehn.triggertrace.ui.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.ui.components.FoodSearchBarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SelectableFood(
    val food: Food,
    val selected: Boolean
)

data class FoodScreenUIState(
    val selectedFoods: List<SelectableFood> = emptyList(),
)

@HiltViewModel
class FoodViewModel @Inject constructor(
    foodRepository: FoodRepository
) : ViewModel() {

    val foodSearchBarViewModel = FoodSearchBarViewModel(foodRepository)

    private val _uiState = MutableStateFlow(FoodScreenUIState())
    val uiState: StateFlow<FoodScreenUIState> = _uiState.asStateFlow()

    // Job reference for background work
    private var debounceJob: MutableMap<SelectableFood, Job> = mutableMapOf()


    fun onFoodSelected(food: Food) {
        foodSearchBarViewModel.updateSearchBarActive(false)

        val containsFood = _uiState.value.selectedFoods.any { it.food == food }
        if (!containsFood) {
            _uiState.update {
                it.copy(
                    selectedFoods = it.selectedFoods + SelectableFood(food, true)
                )
            }
        }
    }

    fun onFoodItemSelectionChanged(selected: Boolean, selectableFood: SelectableFood) {
        // Cancel the previous job if it exists
        debounceJob[selectableFood]?.cancel()

        debounceJob[selectableFood] = viewModelScope.launch {
            // Update the UI state immediately
            _uiState.value = _uiState.value.copy(
                selectedFoods = _uiState.value.selectedFoods.map {
                    if (it.food == selectableFood.food) {
                        it.copy(selected = selected)
                    } else {
                        it
                    }
                }
            )

            delay(1000)

            // Check the updated state and conditionally remove the item
            _uiState.value = _uiState.value.copy(
                selectedFoods = _uiState.value.selectedFoods.filter { item ->
                    item.food != selectableFood.food || item.selected
                }
            )
        }
    }

}