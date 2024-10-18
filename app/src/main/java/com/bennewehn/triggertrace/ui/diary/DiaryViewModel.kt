package com.bennewehn.triggertrace.ui.diary

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Entry
import com.bennewehn.triggertrace.data.FoodEntry
import com.bennewehn.triggertrace.data.FoodEntryRepository
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.data.IName
import com.bennewehn.triggertrace.data.SymptomEntry
import com.bennewehn.triggertrace.data.SymptomEntryRepository
import com.bennewehn.triggertrace.data.SymptomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val listEntries: List<Pair<Entry, IName>> = emptyList(),
    val messageQueue: List<Pair<String, Entry>> = emptyList(),
)

@HiltViewModel
class DiaryViewModel @Inject constructor(
    val foodRepository: FoodRepository,
    val symptomRepository: SymptomRepository,
    val foodEntryRepository: FoodEntryRepository,
    val symptomEntryRepository: SymptomEntryRepository,
    @ApplicationContext val appContext: Context
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

    fun undoDeletion(entry: Entry) {
        viewModelScope.launch{
            when(entry){
                is FoodEntry -> foodEntryRepository.insertFoodEntry(entry)
                is SymptomEntry -> symptomEntryRepository.insertSymptomEntry(entry)
            }
            fetchEntries()
        }
    }

    fun deleteEntry(entry: Entry, name: String){
        viewModelScope.launch{
            when(entry){
                is FoodEntry -> foodEntryRepository.deleteFoodEntry(entry)
                is SymptomEntry -> symptomEntryRepository.deleteSymptomEntry(entry)
            }
            fetchEntries()
            enqueueMessage(
                "$name ${appContext.getString(R.string.deleted)}.",
                entry
            )
        }
    }


    private fun enqueueMessage(message: String, entry: Entry) {
        _uiState.update {
            it.copy(messageQueue = it.messageQueue + Pair(message, entry))
        }
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

    fun showNextMessage() {
        viewModelScope.launch {
            if (_uiState.value.messageQueue.isNotEmpty()) {
                // Remove the first message after it's shown
                _uiState.update {
                    it.copy(
                        messageQueue = it.messageQueue.drop(1)
                    )
                }
            }
        }
    }

}