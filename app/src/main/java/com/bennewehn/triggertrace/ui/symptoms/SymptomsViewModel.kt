package com.bennewehn.triggertrace.ui.symptoms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bennewehn.triggertrace.data.Symptom
import com.bennewehn.triggertrace.data.SymptomEntryRepository
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

data class SymptomsDeletionState(
    val hasConflicts: Boolean = false,
    val showDialog: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val symptom: Symptom? = null
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SymptomsViewModel @Inject constructor(
    private val symptomRepository: SymptomRepository,
    private val symptomEntryRepository: SymptomEntryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SymptomsScreenState())
    val uiState: StateFlow<SymptomsScreenState> = _uiState.asStateFlow()

    private val _deletionState = MutableStateFlow(SymptomsDeletionState())
    val deletionState: StateFlow<SymptomsDeletionState> = _deletionState.asStateFlow()

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

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun dismissDeletionDialog(){
        _deletionState.update {
            it.copy(
                showDialog = false,
            )
        }
    }

    fun deleteSymptom(symptom: Symptom) {
        viewModelScope.launch {
            symptomRepository.deleteSymptom(symptom)
        }
    }

    fun deleteSymptomWithEntries(symptom: Symptom) {
        viewModelScope.launch {
            symptomEntryRepository.deleteAllBySymptomId(symptom.id)
            symptomRepository.deleteSymptom(symptom)
        }
    }

    fun openDeletionConfirmationDialog() {
        _deletionState.update {
            it.copy(
                showConfirmationDialog = true
            )
        }
    }


    fun dismissDeletionConfirmationDialog() {
        _deletionState.update {
            it.copy(
                showConfirmationDialog = false
            )
        }
    }

    fun openDeletionDialog(symptom: Symptom){
        viewModelScope.launch{
            // 1. check if there are any entries in the diary
            val hasConflicts = symptomEntryRepository.getEntriesCount(symptom.id) > 0
            // show dialog
            _deletionState.update {
                it.copy(
                    showDialog = true,
                    hasConflicts = hasConflicts,
                    symptom = symptom
                )
            }
        }
    }
}