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

    fun loadDirectory(path: String) : File {
        val directory = File(path)

        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    fun createProcessedFileFromOrigin(name: String, fileDestinationPath: String): String {
        val parent:File = this.loadOrCreateFolder(fileDestinationPath)
        val file: File = File(parent, "${name}.proc")
        if(file.exists()){
            file.delete()
        }
        return file.absolutePath
    }

    fun loadOrCreateFolder(path: String): File {
        val folder : File = File(path)

        if(!folder.exists()){
            folder.mkdirs()
        }
        return folder;
    }

}