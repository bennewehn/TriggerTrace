package com.bennewehn.triggertrace.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

enum class Scale{
    NUMERIC, CATEGORICAL, BINARY
}

@Serializable
@Entity(
    tableName = "symptom",
    indices = [Index(value = ["name"], unique = true)]
)
data class Symptom(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name="name") override val name: String,
    @ColumnInfo(name="scale") val scale: Scale,
) : IName
