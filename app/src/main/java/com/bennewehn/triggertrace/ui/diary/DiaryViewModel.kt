package com.bennewehn.triggertrace.ui.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.data.Entry
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.data.IName
import com.bennewehn.triggertrace.data.SymptomEntryRepository
import com.bennewehn.triggertrace.data.SymptomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject


data class DiaryUIState(
    val selectedDate: Date = Date(),
    val listEntries: List<Pair<Entry, IName>> = emptyList()
)

@HiltViewModel
class DiaryViewModel @Inject constructor(
    val foodRepository: FoodRepository,
    val symptomRepository: SymptomRepository,
    val foodEntryRepository: FoodEntryRepository,
    val symptomEntryRepository: SymptomEntryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiaryUIState())
    val uiState: StateFlow<DiaryUIState> = _uiState.asStateFlow()

    init{
        fetchEntries()
    }

    fun updateSelectedDate(date: Date) {
        _uiState.update {
            it.copy(
                selectedDate = date
            )
        }

        fetchEntries()
    }

    fun increaseOneDay() {
        val localDate =
            _uiState.value.selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val newDate = localDate.plusDays(1)

        _uiState.update {
            it.copy(
                selectedDate = Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            )
        }

        fetchEntries()
    }

    fun decreaseOneDay() {
        val localDate =
            _uiState.value.selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val newDate = localDate.minusDays(1)

        _uiState.update {
            it.copy(
                selectedDate = Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            )
        }

        fetchEntries()
    }

    private fun fetchEntries() {
        viewModelScope.launch {
            val foods = foodEntryRepository.getFoodEntriesForDay(_uiState.value.selectedDate)
            val symptoms =
                symptomEntryRepository.getSymptomEntriesForDay(_uiState.value.selectedDate)

            val combinedEntries: List<Pair<Entry, IName>> =
                (foods.map { Pair(it, foodRepository.getFoodById(it.foodId)) } +
                        (symptoms.map { Pair(it, symptomRepository.getSymptomById(it.symptomId)) })
                            .sortedBy { it.first.timestamp })

            _uiState.update {
                it.copy(
                    listEntries = combinedEntries
                )
            }

        }
    }

}