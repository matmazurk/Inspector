package com.mat.inspector

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.net.InetAddress

@RunWith(JUnit4::class)
class ConfigurationViewModelTest {

    val fileHandler = mockk<FileHandler>()
    val gson = mockk<Gson>()
    val viewModel = ConfigurationViewModel(fileHandler, gson)
    val context = mockk<Context>()

    @Before
    fun setup() {
        val fileMock = mockk<File>()
        every { context.filesDir } returns fileMock
    }

    @Test
    fun `test get configuration null hash`() {
        val expected = Configuration()
        every { fileHandler.getFileMD5(any()) } returns null

        val actual = viewModel.getConfiguration(context)
        verify { fileHandler.getFileMD5(any()) }

        assert(actual == expected)
    }

    @Test
    fun `test get configuration not null hash`() {
        val expected = Configuration(serverAddress = InetAddress.getByName("8.8.8.8"))
        every { fileHandler.getFileMD5(any()) } returns "hash"
        every { fileHandler.getFileContent(any()) } returns ""
        every { gson.fromJson<Configuration>(any() as JsonReader, Configuration::class.java) } returns expected

        val actual = viewModel.getConfiguration(context)
        verify { fileHandler.getFileMD5(any()) }
        verify { fileHandler.getFileContent(any()) }

        assert(actual == expected)
    }

    @Test
    fun `test get configuration not null hash null file content`() {
        val expected = Configuration()
        every { fileHandler.getFileMD5(any()) } returns "hash"
        every { fileHandler.getFileContent(any()) } returns null
        every { gson.fromJson<Configuration>(any() as JsonReader, Configuration::class.java) } returns expected
        every { gson.toJson(any() as Configuration) } returns "json file"
        every { fileHandler.createNewFile(any(), any()) } returns true

        val actual = viewModel.getConfiguration(context)
        verify { fileHandler.getFileMD5(any()) }
        verify { fileHandler.getFileContent(any()) }
        verify { fileHandler.createNewFile(any(), "json file") }

        assert(expected == actual)
    }

}