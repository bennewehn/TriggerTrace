package com.bennewehn.triggertrace.data

class DefaultSymptomRepository(private val symptomDao: SymptomDao): SymptomRepository {

    override suspend fun insertSymptom(symptom: Symptom): Long {
        return symptomDao.insertSymptom(symptom)
    }

}