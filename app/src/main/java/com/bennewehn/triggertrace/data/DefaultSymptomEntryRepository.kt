package com.bennewehn.triggertrace.data

import com.bennewehn.triggertrace.utils.DateUtils
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
}