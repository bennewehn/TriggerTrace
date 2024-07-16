package com.bennewehn.triggertrace.ui.food

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.data.FoodComposition
import com.bennewehn.triggertrace.data.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddFoodUIState(
    val name: String = "",
    val searchQuery: String = "",
    val searchBarActive: Boolean = false,
    val userMessage: Int? = null,
    val foodPagedData: Flow<PagingData<Food>>? = null,
    val selectedFoods: Set<Food> = emptySet(),
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFoodUIState())
    val uiState: StateFlow<AddFoodUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(300) // Add debounce to avoid excessive calls
                .flatMapLatest { query ->
                    foodRepository.searchItems(query)
                        .cachedIn(viewModelScope)
                }
                .collectLatest { pagingData ->
                    _uiState.update { it.copy(foodPagedData = flowOf(pagingData)) }
                }
        }
    }

    fun addFood() {
        // Check if name is empty
        if (uiState.value.name.isEmpty()) {
            _uiState.update {
                it.copy(userMessage = R.string.empty_food_name_message)
            }
            return
        }
        // Add Food to db
        viewModelScope.launch {
            try {
                val food = Food(name = uiState.value.name)
                // make this a transaction
                foodRepository.insertFood(food)
                uiState.value.selectedFoods.forEach {
                    foodRepository.insertFoodWithComposedFoods(FoodComposition(food.id, it.id))
                }
            } catch (e: SQLiteConstraintException) {
                _uiState.update {
                    it.copy(userMessage = R.string.food_name_used_message)
                }
            } catch (e: Exception) {
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
                searchBarActive = false
            )
        }
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

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun updateSearchBarActive(active: Boolean) {
        _uiState.update {
            it.copy(searchBarActive = active)
        }
    }

}