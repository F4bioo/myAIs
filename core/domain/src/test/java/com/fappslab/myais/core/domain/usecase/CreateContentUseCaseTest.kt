package com.fappslab.myais.core.domain.usecase

import app.cash.turbine.test
import com.fappslab.myais.core.domain.model.Description
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

internal class CreateContentUseCaseTest {

    private val repository: MyAIsRepository = mockk()
    private val subject = CreateContentUseCase(repository)

    @Test
    fun `Given valid content When invoke is called Then should return description`() = runTest {
        // Given
        val description = Description("Sample description")
        every { repository.generateContent(any(), any()) } returns flowOf(description)

        // When
        val result = subject {
            text("Sample text")
            image("encodedImageString")
        }

        // Then
        result.test {
            assertEquals(description, awaitItem())
            awaitComplete()
        }
        verify { repository.generateContent(any(), any()) }
    }


    @Test
    fun `Given repository throws exception When invoke is called Then should propagate exception`() =
        runTest {
            // Given
            val expectedMessage = "Test exception"
            val cause = Throwable(expectedMessage)
            every { repository.generateContent(any(), any()) } returns flow { throw cause }

            // Then
            val result = assertFailsWith<Throwable> {
                subject {
                    text("Sample text")
                    image("encodedImageString")
                }.collect()
            }
            assertEquals(expectedMessage, result.message)
            verify { repository.generateContent(any(), any()) }
        }
}
