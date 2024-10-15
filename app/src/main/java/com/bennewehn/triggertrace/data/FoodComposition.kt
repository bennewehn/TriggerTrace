package com.bennewehn.triggertrace.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "food_composition",
    primaryKeys = ["foodId", "composedFoodId"],
    foreignKeys = [
        ForeignKey(entity = Food::class, parentColumns = ["id"], childColumns = ["foodId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Food::class, parentColumns = ["id"], childColumns = ["composedFoodId"], onDelete = ForeignKey.RESTRICT)
    ],
    indices = [Index(value = ["foodId"]), Index(value = ["composedFoodId"])]
)
data class FoodComposition(
    val foodId: Long,
    val composedFoodId: Long
)