package com.fappslab.myais.core.domain.usecase

import app.cash.turbine.test
import com.fappslab.myais.core.domain.model.PromptType
import com.fappslab.myais.core.domain.repository.MyAIsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class GetPromptUseCaseTest {

    private val repository: MyAIsRepository = mockk()
    private val subject = GetPromptUseCase(repository)

    @Test
    fun `Given valid promptType When invoke is called Then should return prompt`() = runTest {
        val promptType = PromptType.ImageDescription
        val expectedPrompt = "Sample prompt"
        every { repository.getPrompt(promptType) } returns flowOf(expectedPrompt)

        val result = subject(promptType)

        result.test {
            assertEquals(expectedPrompt, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Given repository throws exception When invoke is called Then should propagate exception`() = runTest {
        val promptType = PromptType.ImageDescription
        val expectedMessage = "Test exception"
        val cause = Throwable(expectedMessage)
        every { repository.getPrompt(promptType) } returns flow { throw cause }

        val result = assertFailsWith<Throwable> {
            subject(promptType).collect()
        }
        assertEquals(expectedMessage, result.message)
    }
}