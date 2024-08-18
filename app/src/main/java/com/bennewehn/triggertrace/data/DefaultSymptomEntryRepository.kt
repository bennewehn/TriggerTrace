package com.bennewehn.triggertrace.data

class DefaultSymptomEntryRepository(private val symptomEntryDao: SymptomEntryDao): SymptomEntryRepository {
    override suspend fun insertSymptomEntry(symptomEntry: SymptomEntry): Long {
        return symptomEntryDao.insertSymptomEntry(symptomEntry)
    }
}