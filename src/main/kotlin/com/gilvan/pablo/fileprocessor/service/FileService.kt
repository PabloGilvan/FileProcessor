package com.gilvan.pablo.fileprocessor.service

import com.gilvan.pablo.fileprocessor.utils.FileManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class FileService(
        val dataProcessorService: DataProcessorService
){
    val fileManager: FileManager = FileManager()

    @Value("\${origin.file.import}")
    private val fileOriginPath: String? = null

    @Value("\${origin.file.export}")
    private val fileDestinationPath: String? = null

    val logger = LoggerFactory.getLogger(javaClass)

    fun processFiles() {
        if(Objects.nonNull(fileOriginPath) && Objects.nonNull(fileDestinationPath)) {
            fileManager.importDatFiles(fileOriginPath!!)
                       .forEach(this::loadFileContent)
        } else {
            logger.warn("Diretórios de importação/exportação não definidos.")
        }
    }

    fun loadFileContent(file: File){
        val reader: Reader = Files.newBufferedReader(Paths.get(file.absolutePath))
        reader.use {
            reader.forEachLine { dataProcessorService.processContent(it) }
        }
        this.closeProcess(file)
    }

    private fun closeProcess(file: File) {
        val writer = Files.newBufferedWriter(Paths.get(fileManager.createProcessedFileFromOrigin(file.name, fileDestinationPath!!)))
        writer.use {
            dataProcessorService.summary().forEach{ writer.appendln(it) }
            writer.flush()
        }
        dataProcessorService.clear()
    }
}