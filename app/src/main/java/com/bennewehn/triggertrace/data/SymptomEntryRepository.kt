package com.bennewehn.triggertrace.data

interface SymptomEntryRepository {
    suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long

    suspend fun getEntriesCount(symptomId: Long): Long

    suspend fun deleteAllBySymptomId(symptomId: Long)
}