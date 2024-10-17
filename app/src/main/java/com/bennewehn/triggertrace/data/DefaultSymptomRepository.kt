package com.bennewehn.triggertrace.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class DefaultSymptomRepository(private val symptomDao: SymptomDao) : SymptomRepository {

    override suspend fun insertSymptom(symptom: Symptom): Long {
        return symptomDao.insertSymptom(symptom)
    }

    override fun searchItems(query: String): Flow<PagingData<Symptom>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { symptomDao.searchItems(query) }
        ).flow
    }

    override suspend fun deleteSymptom(symptom: Symptom) {
        symptomDao.deleteSymptom(symptom)
    }

    override suspend fun updateSymptom(symptom: Symptom) {
        symptomDao.updateSymptom(symptom)
    }

}