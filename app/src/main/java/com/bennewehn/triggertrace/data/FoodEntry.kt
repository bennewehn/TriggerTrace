package com.bennewehn.triggertrace.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "food_entries",
    foreignKeys = [ForeignKey(
        entity = Food::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("foodId"),
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index(value = ["foodId"])]
)
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name="foodId") val foodId: Long,
    @ColumnInfo(name="timestamp") override val timestamp: Date = Date(),
) : Entry