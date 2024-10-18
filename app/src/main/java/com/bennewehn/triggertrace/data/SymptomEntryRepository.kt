package com.bennewehn.triggertrace.data

import java.util.Date

interface SymptomEntryRepository {
    suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long

    suspend fun getEntriesCount(symptomId: Long): Long

    suspend fun deleteAllBySymptomId(symptomId: Long)

    suspend fun getSymptomEntriesForDay(day: Date): List<SymptomEntry>
}