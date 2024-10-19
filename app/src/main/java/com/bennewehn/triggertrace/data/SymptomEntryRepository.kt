package com.bennewehn.triggertrace.data

import android.content.ContentResolver
import android.net.Uri
import java.util.Date

interface SymptomEntryRepository {
    suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long

    suspend fun getEntriesCount(symptomId: Long): Long

    suspend fun deleteAllBySymptomId(symptomId: Long)

    suspend fun getSymptomEntriesForDay(day: Date): List<SymptomEntry>

    suspend fun deleteSymptomEntry(symptomEntry: SymptomEntry)

    suspend fun exportSymptomEntriesToDirectory(directoryUri: Uri, contentResolver: ContentResolver)
}