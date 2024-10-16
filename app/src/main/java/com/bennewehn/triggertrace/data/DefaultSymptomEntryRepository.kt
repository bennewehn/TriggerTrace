package com.bennewehn.triggertrace.data

class DefaultSymptomEntryRepository(private val symptomEntryDao: SymptomEntryDao): SymptomEntryRepository {
    override suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long {
        return symptomEntryDao.insertSymptomEntry(symptomEntry)
    }

    override suspend fun getEntriesCount(symptomId: Long): Long {
        return symptomEntryDao.getEntriesCount(symptomId)
    }

    override suspend fun deleteAllBySymptomId(symptomId: Long) {
        symptomEntryDao.deleteAllBySymptomId(symptomId)
    }
}