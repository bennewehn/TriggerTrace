package com.bennewehn.triggertrace.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bennewehn.triggertrace.data.Food
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

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class FoodSearchBarViewModel(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodSearchBarState())
    val uiState: StateFlow<FoodSearchBarState> = _uiState.asStateFlow()


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

}