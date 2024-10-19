package com.bennewehn.triggertrace.data

import android.content.ContentResolver
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bennewehn.triggertrace.utils.FileUtils.createCSVFileInDirectory
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

    override suspend fun getSymptomById(symptomId: Long): Symptom {
        return symptomDao.getSymptomById(symptomId)
    }

    override suspend fun exportSymptomsToDirectory(
        directoryUri: Uri,
        contentResolver: ContentResolver
    ) {
        val fileUri = createCSVFileInDirectory(contentResolver, directoryUri, "symptoms.csv")
        fileUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.writer().use { writer ->
                    writer.append("id, name, scale\n") // CSV Header
                    val symptoms = symptomDao.getAllSymptoms()
                    symptoms.forEach { symptom ->
                        writer.append("${symptom.id}, ${symptom.name}, ${symptom.scale}\n")
                    }
                }
            }
        }
    }


}