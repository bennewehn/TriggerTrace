package com.bennewehn.triggertrace.ui.food

import androidx.lifecycle.ViewModel
import com.bennewehn.triggertrace.data.FoodRepository
import com.bennewehn.triggertrace.ui.components.FoodSearchBarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    foodRepository: FoodRepository
): ViewModel() {

    val foodSearchBarViewModel = FoodSearchBarViewModel(foodRepository)

}