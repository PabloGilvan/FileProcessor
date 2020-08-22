package com.gilvan.pablo.fileprocessor.utils

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

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


    @Test fun `should return only dat file `(){
        val directory:File = Mockito.mock(File::class.java)
        Mockito.`when`(directory.exists()).thenReturn(true)
        Mockito.`when`(directory.listFiles()).thenReturn(fileList.toTypedArray())

        val result = fileManager.filterDatFiles(directory)
        assertThat("Não ser nulo", result, CoreMatchers.notNullValue())
        assertThat("Possuí somente 2 arquivos", result.size, CoreMatchers.equalTo(2))
    }
}