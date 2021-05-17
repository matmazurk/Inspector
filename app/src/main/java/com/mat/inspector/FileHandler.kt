package com.mat.inspector

import android.content.Context

interface FileHandler {
    fun getFileMD5(filePath: String): String?
    fun getFileContent(filePath: String): String?
    fun createNewFile(filePath: String, content: String): Boolean
}