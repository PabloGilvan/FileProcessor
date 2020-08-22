package com.gilvan.pablo.fileprocessor.job

import com.gilvan.pablo.fileprocessor.service.FileService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class JobComponent(
        val fileService: FileService
) {

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    fun importFiles() {
        fileService.processFiles()
    }
}