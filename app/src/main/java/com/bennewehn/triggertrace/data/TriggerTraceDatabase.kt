package com.bennewehn.triggertrace.data

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import java.io.IOException


@Database(entities = [Food::class, FoodComposition::class], version = 1)
abstract class TriggerTraceDatabase(): RoomDatabase() {
    abstract val foodDao: FoodDao
    abstract val triggerTraceDatabaseDao: TriggerTraceDatabaseDao

    /**
     * Backup the database
     */
    suspend fun backupDatabase(context: Context, uri: Uri){
        val dbFile = context.getDatabasePath("trigger_trace_db")
        checkpoint()
        try {
            // copy backup to destination
            val contentResolver = context.contentResolver
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                dbFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private suspend fun checkpoint() {
        triggerTraceDatabaseDao.checkpoint(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))
    }

}