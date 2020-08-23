package com.gilvan.pablo.fileprocessor.service

import com.gilvan.pablo.fileprocessor.utils.FileManager
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.io.File

@RunWith(SpringJUnit4ClassRunner::class)
class FileServiceTest{

    @InjectMocks
    lateinit var fileService: FileService;

    @Mock
    lateinit var dataProcessorService: DataProcessorService


    @Test fun `should call 'loadFileContent' the correct number of times`(){
        var fileManager: FileManager = mock(FileManager::class.java)

        Mockito.`when`(fileManager.importDatFiles(anyString())).thenReturn(arrayListOf(File("one")))
        Mockito.`when`(dataProcessorService.summary()).thenReturn(emptyList())
        fileService.processFiles()
    }
}