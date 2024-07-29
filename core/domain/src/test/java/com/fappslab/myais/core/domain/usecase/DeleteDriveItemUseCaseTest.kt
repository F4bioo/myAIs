package com.fappslab.myais.core.domain.usecase

import app.cash.turbine.test
import com.fappslab.myais.core.domain.model.DriverItemType
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class DeleteDriveItemUseCaseTest {

    private val repository: MyAIsRepository = mockk()
    private val subject = DeleteDriveItemUseCase(repository)

    @Test
    fun `Given valid itemType When invoke is called Then should return true`() = runTest {
        // Given
        val itemType = DriverItemType.File(fileId = "SampleType")
        every { repository.deleteDriveItem(itemType) } returns flowOf(true)

        // When
        val result = subject(itemType)

        // Then
        result.test {
            assertEquals(true, awaitItem())
            awaitComplete()
        }
        verify { repository.deleteDriveItem(itemType) }
    }

    @Test
    fun `Given invalid itemType When invoke is called Then should return false`() = runTest {
        // Given
        val itemType = DriverItemType.File(fileId = "InvalidType")
        every { repository.deleteDriveItem(itemType) } returns flowOf(false)

        // When
        val result = subject(itemType)

        // Then
        result.test {
            assertEquals(false, awaitItem())
            awaitComplete()
        }
        verify { repository.deleteDriveItem(itemType) }
    }

    @Test
    fun `Given repository throws exception When invoke is called Then should propagate exception`() =
        runTest {
            // Given
            val expectedMessage = "Test exception"
            val cause = Throwable(expectedMessage)
            val itemType = DriverItemType.File(fileId = "sample_file_id")
            every { repository.deleteDriveItem(itemType) } returns flow { throw cause }

            // Then
            val result = assertFailsWith<Throwable> {
                subject(itemType).collect()
            }
            assertEquals(expectedMessage, result.message)
            verify { repository.deleteDriveItem(itemType) }
        }
}
