package com.bennewehn.triggertrace.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "food",
    indices = [Index(value = ["name"], unique = true)])
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name="name") val name: String,
)

data class FoodWithComposedFoods(
    @Embedded val food: Food,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(FoodComposition::class, parentColumn = "foodId", entityColumn = "composedFoodId")
    )
    val composedFoods: List<Food>
)