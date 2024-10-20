package com.bennewehn.triggertrace.data

import android.content.ContentResolver
import android.net.Uri
import com.bennewehn.triggertrace.utils.DateUtils
import com.bennewehn.triggertrace.utils.FileUtils.createCSVFileInDirectory
import java.util.Date

class DefaultSymptomEntryRepository(private val symptomEntryDao: SymptomEntryDao) :
    SymptomEntryRepository {
    override suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long {
        return symptomEntryDao.insertSymptomEntry(symptomEntry)
    }

    override suspend fun getEntriesCount(symptomId: Long): Long {
        return symptomEntryDao.getEntriesCount(symptomId)
    }

    override suspend fun deleteAllBySymptomId(symptomId: Long) {
        symptomEntryDao.deleteAllBySymptomId(symptomId)
    }

    override suspend fun getSymptomEntriesForDay(day: Date): List<SymptomEntry> {
        return symptomEntryDao.getSymptomEntriesForDay(
            DateUtils.getStartOfDay(day),
            DateUtils.getEndOfDay(day)
        )
    }

    override suspend fun deleteSymptomEntry(symptomEntry: SymptomEntry) {
        symptomEntryDao.deleteSymptomEntry(symptomEntry)
    }

    override suspend fun exportSymptomEntriesToDirectory(
        directoryUri: Uri,
        contentResolver: ContentResolver
    ) {
        val fileUri = createCSVFileInDirectory(contentResolver, directoryUri, "symptom_entries.csv")
        fileUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.writer().use { writer ->
                    writer.append("id, symptomId, timestamp, scaleValue\n") // CSV Header
                    val symptoms = symptomEntryDao.getAllSymptomEntries()
                    symptoms.forEach { symptom ->
                        writer.append("${symptom.id}, ${symptom.symptomId}, ${symptom.timestamp}, ${symptom.scaleValue}\n")
                    }
                }
            }
        }
    }
}