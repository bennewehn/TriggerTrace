package com.bennewehn.triggertrace.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennewehn.triggertrace.data.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MealRepository): ViewModel() {

    //val mealList = repository.getMealList()

    fun buttonClicked() {
        viewModelScope.launch {
        }
    }
}