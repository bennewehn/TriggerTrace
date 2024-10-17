package com.bennewehn.triggertrace.ui.symptoms

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.R
import com.bennewehn.triggertrace.data.Scale
import com.bennewehn.triggertrace.data.Symptom
import com.bennewehn.triggertrace.data.SymptomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AddSymptomUIState(
    val name: String = "",
    val scale: Scale? = null,
    val userMessage: Int? = null,
    val symptomAddedSuccessfully: Boolean = false
)

@HiltViewModel
class AddSymptomViewModel @Inject constructor(
    private val symptomRepository: SymptomRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AddSymptomUIState())
    val uiState: StateFlow<AddSymptomUIState> = _uiState.asStateFlow()

    fun onAddSymptom(){
        // name cannot be empty
        if(uiState.value.name.isEmpty()){
            _uiState.update {
                it.copy(userMessage = R.string.name_cannot_be_empty_message)
            }
            return
        }
        // scale must be selected
        if(uiState.value.scale == null){
            _uiState.update {
                it.copy(userMessage = R.string.no_scale_selected_message)
            }
            return
        }

        val sym = Symptom(name = uiState.value.name, scale = uiState.value.scale!!)

        viewModelScope.launch {
            try{
                symptomRepository.insertSymptom(sym)
                _uiState.update {
                    it.copy(symptomAddedSuccessfully = true)
                }
            }
            catch (_: SQLiteConstraintException) {
                _uiState.update {
                    it.copy(userMessage = R.string.name_already_used_message)
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(userMessage = R.string.error_message_adding_food)
                }
            }
        }
    }

    fun updateName(name: String){
        _uiState.update {
            it.copy(name = name)
        }
    }

    fun setScale(scale: Scale){
        _uiState.update {
            it.copy(
                scale = scale
            )
        }
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

}