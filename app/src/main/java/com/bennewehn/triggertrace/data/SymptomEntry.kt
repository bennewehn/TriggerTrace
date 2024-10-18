package com.bennewehn.triggertrace.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "symptom_entries",
    foreignKeys = [ForeignKey(
        entity = Symptom::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("symptomId"),
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index(value = ["symptomId"])]
)
data class SymptomEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name="symptomId") val symptomId: Long,
    @ColumnInfo(name="timestamp") override val timestamp: Date = Date(),
    @ColumnInfo(name="scaleValue") val scaleValue: Int,
) : Entry
