package com.gilvan.pablo.fileprocessor.utils

import java.io.File

open class FileManager {

    fun importDatFiles(path: String): List<File> {
        val directory = loadDirectory(path)
        return this.filterDatFiles(directory)
    }

    fun filterDatFiles(directory: File): List<File> {
        return directory.listFiles().filter { file -> file.name.endsWith(".dat") }
    }

    private fun loadDirectory(path: String) : File {
        val directory = File(path)

        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    fun createProcessedFileFromOrigin(path: String): String {
        val file: File = File("${path}.proc")
        if(file.exists()){
            file.delete()
        }
        file.createNewFile()
        return file.absolutePath
    }

}