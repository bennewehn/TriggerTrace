package com.bennewehn.triggertrace.ui.food

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(): ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchBarActive= MutableStateFlow(false)
    val isSearchBarActive: StateFlow<Boolean> = _isSearchBarActive.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateIsSearchBarActive(active: Boolean) {
        _isSearchBarActive.value = active
    }

}