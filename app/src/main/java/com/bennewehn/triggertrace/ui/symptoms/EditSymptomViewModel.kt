package com.bennewehn.triggertrace.ui.symptoms

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.SymptomRepository
import com.bennewehn.triggertrace.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class EditSymptomUIState(
    val name: String = "",
    val userMessage: Int? = null,
    val symptomEditedSuccessfully: Boolean = false
)

@HiltViewModel
class EditSymptomViewModel @Inject constructor(
    private val symptomRepository: SymptomRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val symptom = Screen.EditSymptomScreen.from(savedStateHandle).symptom

    private val _uiState = MutableStateFlow(
        EditSymptomUIState(
            name = symptom.name
        )
    )
    val uiState: StateFlow<EditSymptomUIState> = _uiState.asStateFlow()


    fun updateName(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    fun updateSymptom() {
        viewModelScope.launch {
            try{
                symptomRepository.updateSymptom(symptom.copy(name = _uiState.value.name))
                _uiState.update {
                    it.copy(symptomEditedSuccessfully = true)
                }
            }
            catch (_: SQLiteConstraintException) {
                _uiState.update {
                    it.copy(userMessage = R.string.name_already_used_message)
                }
            }
        }
    }

}