package com.mat.inspector

import android.content.Context
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

class FileRepository : FileHandler {

    override fun getFileMD5(filePath: String): String? {
        val file = File(filePath)
        if (!file.exists()) {
            return null
        }
        val fileContent = file.readText()
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(fileContent.toByteArray())).toString(16).padStart(32, '0')
    }

    override fun getFileContent(filePath: String): String? {
        val file = File(filePath)
        if (!file.exists()) {
            return null
        }
        return file.readText()
    }

    /*
        overrides file content if file exists
     */
    override fun createNewFile(filePath: String, content: String): Boolean {
        var result = true
        val file = File(filePath)
        if (!file.exists()) {
            result = file.createNewFile()
        }
        if (result) {
            file.writeText(content)
        }
        return result
    }

}