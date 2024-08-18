package com.bennewehn.triggertrace.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface SymptomDao {
    @Insert
    suspend fun insertSymptom(symptom: Symptom): Long
}