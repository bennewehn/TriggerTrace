package com.bennewehn.triggertrace.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "food_composition",
    primaryKeys = ["foodId", "composedFoodId"],
    foreignKeys = [
        ForeignKey(entity = Food::class, parentColumns = ["id"], childColumns = ["foodId"], onDelete = ForeignKey.RESTRICT),
        ForeignKey(entity = Food::class, parentColumns = ["id"], childColumns = ["composedFoodId"], onDelete = ForeignKey.RESTRICT)
    ]
)
data class FoodComposition(
    val foodId: Int,
    val composedFoodId: Int
)