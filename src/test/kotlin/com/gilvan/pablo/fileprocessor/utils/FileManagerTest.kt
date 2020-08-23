package com.gilvan.pablo.fileprocessor.utils

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.io.File

@RunWith(SpringJUnit4ClassRunner::class)
class FileManagerTest {

    val fileList: MutableList<File> = mutableListOf();
    val fileManager = FileManager()

    @Before
    fun setUp() {
        fileList.addAll(listOf(File("file1.ext"), File("file2.dat"), File("file3.ext"), File("file4.dat")))
    }

    @Test fun `import dat files should call filter, always`(){
        val path = "/path/directory";

        val directory:File = mock(File::class.java)
        `when`(directory.exists()).thenReturn(true)
        `when`(directory.absolutePath).thenReturn(path)
        `when`(directory.listFiles()).thenReturn(emptyArray())

        val fileManager2: FileManager = spy(FileManager::class.java)

        doReturn(directory).`when`(fileManager2).loadOrCreateFolder(anyString())

        fileManager.importDatFiles(path)
        verify(fileManager.filterDatFiles(ArgumentMatchers.any(File::class.java)), atMostOnce())
    }

    @Test fun `should return only dat file `(){
        val directory:File = mock(File::class.java)
        `when`(directory.exists()).thenReturn(true)
        `when`(directory.listFiles()).thenReturn(fileList.toTypedArray())

        val result = fileManager.filterDatFiles(directory)
        assertThat("Não ser nulo", result, CoreMatchers.notNullValue())
        assertThat("Possuí somente 2 arquivos", result.size, CoreMatchers.equalTo(2))
    }

    @Test fun `should create a file with the same name but ending with proc`(){

        val nameFile = "name";
        val path = "/path/directory";

        val directory:File = mock(File::class.java)
        `when`(directory.exists()).thenReturn(true)
        `when`(directory.absolutePath).thenReturn(path)

        val fileManager2: FileManager = spy(FileManager::class.java)
        doReturn(directory).`when`(fileManager2).loadOrCreateFolder(anyString())

        val result = fileManager.createProcessedFileFromOrigin(nameFile, path)
        assertThat("Não ser nulo", result, CoreMatchers.notNullValue())
        assertThat("Possuí o valor", result, CoreMatchers.equalTo("${path}${File.separator}${nameFile}.proc"))
    }
}