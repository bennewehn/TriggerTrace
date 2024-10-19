package com.bennewehn.triggertrace.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.DocumentsContract

object FileUtils {
    fun createCSVFileInDirectory(
        contentResolver: ContentResolver,
        directoryUri: Uri,
        fileName: String
    ): Uri? {
        val documentUri = DocumentsContract.buildDocumentUriUsingTree(
            directoryUri,
            DocumentsContract.getTreeDocumentId(directoryUri)
        )
        return DocumentsContract.createDocument(contentResolver, documentUri, "text/csv", fileName)
    }
}