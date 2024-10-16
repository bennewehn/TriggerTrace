package com.bennewehn.triggertrace.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SymptomDao {
    @Insert
    suspend fun insertSymptom(symptom: Symptom): Long

    @Query("SELECT * FROM symptom WHERE name LIKE '%' || :query || '%'")
    fun searchItems(query: String): PagingSource<Int, Symptom>

    @Delete
    suspend fun deleteSymptom(symptom: Symptom)
}