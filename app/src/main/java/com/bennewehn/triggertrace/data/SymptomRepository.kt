package com.bennewehn.triggertrace.data

import android.content.ContentResolver
import android.net.Uri
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface SymptomRepository {
    suspend fun insertSymptom(symptom: Symptom): Long

    fun searchItems(query: String): Flow<PagingData<Symptom>>

    suspend fun deleteSymptom(symptom: Symptom)

    suspend fun updateSymptom(symptom: Symptom)

    suspend fun getSymptomById(symptomId: Long) : Symptom

    suspend fun exportSymptomsToDirectory(directoryUri: Uri, contentResolver: ContentResolver)
}