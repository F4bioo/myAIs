package com.fappslab.myais.core.domain.usecase

import app.cash.turbine.test
import com.fappslab.myais.core.domain.model.Memory
import com.fappslab.myais.core.domain.model.SaveMemory
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class UploadDriveFileUseCaseTest {

    private val repository: MyAIsRepository = mockk()
    private val subject = UploadDriveFileUseCase(repository)

    @Test
    fun `Given valid save memory When invoke is called Then should return memory`() = runTest {
        val saveMemory = saveMemoryStub
        val expectedMemory = memoryStub
        every { repository.uploadDriveFile(saveMemory) } returns flowOf(expectedMemory)

        val result = subject(saveMemory)

        result.test {
            assertEquals(expectedMemory, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Given repository throws exception When invoke is called Then should propagate exception`() =
        runTest {
            val saveMemory = saveMemoryStub
            val expectedMessage = "Test exception"
            val cause = Throwable(expectedMessage)
            every { repository.uploadDriveFile(saveMemory) } returns flow { throw cause }

            val result = assertFailsWith<Throwable> {
                subject(saveMemory).collect()
            }
            assertEquals(expectedMessage, result.message)
        }
}

private val saveMemoryStub = SaveMemory(
    description = "Marcela",
    mimeType = "Marcelina",
    fileImage = File("Some path")
)

private val memoryStub = Memory(
    id = "Lindsay",
    size = 9742L,
    fileName = "Sherina",
    mimeType = "Leana",
    description = "Ursula",
    createdTime = "Edson",
    webViewLink = "Margaux",
    webContentLink = "Patience",
    thumbnailLink = "Gerrit",
    parentFolderId = "Karey"
)
