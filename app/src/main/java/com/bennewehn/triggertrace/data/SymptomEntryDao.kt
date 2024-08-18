package com.bennewehn.triggertrace.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface SymptomEntryDao {
    @Insert
    suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long
}