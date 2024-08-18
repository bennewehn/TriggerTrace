package com.bennewehn.triggertrace.data

interface SymptomEntryRepository {
    suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long
}