package com.bennewehn.triggertrace.ui.food

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(): ViewModel() {

    //private val _name = MutableLiveData("")
    //val name: LiveData<String> = _name

    var name1 by mutableStateOf("hello")

    var name2 = MutableStateFlow("name2")

    fun updateName2(newState: String) {
        name2.value = newState
    }

}