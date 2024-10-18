package com.bennewehn.triggertrace.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "food_inclusions",
    primaryKeys = ["foodId", "includedFoodId"],
    foreignKeys = [
        ForeignKey(entity = Food::class, parentColumns = ["id"], childColumns = ["foodId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Food::class, parentColumns = ["id"], childColumns = ["includedFoodId"], onDelete = ForeignKey.RESTRICT)
    ],
    indices = [Index(value = ["foodId"]), Index(value = ["includedFoodId"])]
)
data class FoodInclusion(
    val foodId: Long,
    val includedFoodId: Long
)