package com.bennewehn.triggertrace.ui.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bennewehn.triggertrace.data.Food
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.data.FoodRepository
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

data class FoodSearchBarState(
    val searchQuery: String = "",
    val searchBarActive: Boolean = false,
    val foodPagedData: Flow<PagingData<Food>>? = null
)

data class FoodDeletionDialogState(
    val showDialog: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val conflictFoods: List<Food> = emptyList(),
    val foodExitsInDiary: Boolean = false,
    val food: Food? = null
)

data class EditFoodDialogState(
    val showEditScreen: Boolean = false,
    val food: Food? = null,
    val parentIds: List<Long> = emptyList(),
    val children: List<Food> = emptyList()
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class FoodSearchBarViewModel(
    private val foodEntryRepository: FoodEntryRepository,
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodSearchBarState())
    val uiState: StateFlow<FoodSearchBarState> = _uiState.asStateFlow()

    private val _deletionDialogState = MutableStateFlow(FoodDeletionDialogState())
    val deletionDialogState: StateFlow<FoodDeletionDialogState> = _deletionDialogState.asStateFlow()

    private val _editFoodState = MutableStateFlow(EditFoodDialogState())
    val editFoodState: StateFlow<EditFoodDialogState> = _editFoodState.asStateFlow()

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

    fun dismissDeletionDialog() {
        _deletionDialogState.update {
            it.copy(
                showDialog = false
            )
        }
    }

    fun openDeletionDialog(food: Food) {
        viewModelScope.launch {
            // solve dependencies
            // 1. check if food is included in other foods
            val ids = foodRepository.getFoodIdsWhereIncluded(food.id)

            if (ids.isNotEmpty()) {
                val conflicts = foodRepository.getFoodsByIds(ids)
                _deletionDialogState.update {
                    it.copy(conflictFoods = conflicts)
                }
            } else {
                // clear list
                _deletionDialogState.update {
                    it.copy(conflictFoods = emptyList())
                }
            }

            // 2. check if food is used in database
            val foodExitsInDiary = foodEntryRepository.foodEntryCount(food.id) > 0

            // show dialog
            _deletionDialogState.update {
                it.copy(
                    showDialog = true,
                    food = food,
                    foodExitsInDiary = foodExitsInDiary
                )
            }

        }
    }

    fun openDeletionConfirmationDialog() {
        _deletionDialogState.update {
            it.copy(
                showDialog = false,
                showConfirmationDialog = true
            )
        }
    }

    fun dismissEditDialog(){
        _editFoodState.update {
            it.copy(
                showEditScreen = false,
            )
        }
    }

    fun dismissDeletionConfirmationDialog() {
        _deletionDialogState.update {
            it.copy(
                showConfirmationDialog = false
            )
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            foodRepository.deleteFood(food)
        }
    }

    fun editFood(food: Food){
        // get all sub-inclusions
        viewModelScope.launch{
            // get all parents
            val allParents = mutableListOf<Long>()
            getAllParents(food.id, allParents)

            val childrenIds = foodRepository.getInclusions(food.id)
            val children = foodRepository.getFoodsByIds(childrenIds)

            _editFoodState.update {
                it.copy(
                    showEditScreen = true,
                    food = food,
                    parentIds = allParents,
                    children = children
                )
            }
        }
    }

    private suspend fun getAllParents(foodId: Long, result: MutableList<Long>) {
        val parents = foodRepository.getParents(foodId)

        for (parent in parents) {
            if (!result.contains(parent)) {
                result.add(parent)
                // Recursively get all parents of the current food
                getAllParents(parent, result)
            }
        }
    }

    fun deleteFoodWithDiaryEntries(food: Food) {
        viewModelScope.launch {
            // 1. Delete all diary entries
            foodEntryRepository.deleteAllByFoodId(food.id)
            // 2. Delete food
            foodRepository.deleteFood(food)
        }
    }

}
