package com.bennewehn.triggertrace.ui.symptoms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bennewehn.triggertrace.data.Symptom
import com.bennewehn.triggertrace.data.SymptomRepository
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


data class SymptomsScreenState(
    val searchQuery: String = "",
    val symptomPagedData: Flow<PagingData<Symptom>>? = null
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SymptomsViewModel @Inject constructor(
    private val symptomRepository: SymptomRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SymptomsScreenState())
    val uiState: StateFlow<SymptomsScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(300) // Add debounce to avoid excessive calls
                .flatMapLatest { query ->
                    symptomRepository.searchItems(query)
                        .cachedIn(viewModelScope)
                }
                .collectLatest { pagingData ->
                    _uiState.update { it.copy(symptomPagedData = flowOf(pagingData)) }
                }
        }
    }

    fun onSymptomSelected(symptom: Symptom){

    }

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }
}