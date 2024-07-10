package com.bennewehn.triggertrace.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Meal(
    @PrimaryKey val id: Int,
    @ColumnInfo(name="name") val name: String?,
)