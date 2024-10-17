package com.bennewehn.triggertrace.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface SymptomRepository {
    suspend fun insertSymptom(symptom: Symptom): Long

    fun searchItems(query: String): Flow<PagingData<Symptom>>

    suspend fun deleteSymptom(symptom: Symptom)

    suspend fun updateSymptom(symptom: Symptom)
}