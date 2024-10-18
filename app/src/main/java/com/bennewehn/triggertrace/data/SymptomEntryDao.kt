package com.bennewehn.triggertrace.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SymptomEntryDao {
    @Insert
    suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long

    @Delete
    suspend fun deleteSymptomEntry(symptomEntry: SymptomEntry)

    @Query("SELECT COUNT(*) FROM symptom_entries WHERE symptomId = :symptomId")
    suspend fun getEntriesCount(symptomId: Long): Long

    @Query("DELETE FROM symptom_entries WHERE symptomId = :symptomId")
    suspend fun deleteAllBySymptomId(symptomId: Long)

    @Query("SELECT * FROM symptom_entries WHERE timestamp BETWEEN :startOfDay AND :endOfDay")
    suspend fun getSymptomEntriesForDay(startOfDay: Long, endOfDay: Long): List<SymptomEntry>
}