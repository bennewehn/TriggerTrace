package com.bennewehn.triggertrace.data

interface SymptomRepository {
    suspend fun insertSymptom(symptom: Symptom): Long
}