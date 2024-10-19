package com.bennewehn.triggertrace.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SymptomDao {
    @Insert
    suspend fun insertSymptom(symptom: Symptom): Long

    @Query("SELECT * FROM symptom WHERE name LIKE '%' || :query || '%'")
    fun searchItems(query: String): PagingSource<Int, Symptom>

    @Delete
    suspend fun deleteSymptom(symptom: Symptom)

    @Update
    suspend fun updateSymptom(symptom: Symptom)

    @Query("SELECT * from symptom where id = :symptomId")
    suspend fun getSymptomById(symptomId: Long) : Symptom

    @Query("SELECT * FROM symptom")
    suspend fun getAllSymptoms(): List<Symptom>
}