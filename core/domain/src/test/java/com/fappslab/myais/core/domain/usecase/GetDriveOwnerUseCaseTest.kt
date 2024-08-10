package com.fappslab.myais.core.domain.usecase

import app.cash.turbine.test
import com.fappslab.myais.core.domain.model.Owner
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

internal class GetDriveOwnerUseCaseTest {

    private val repository: MyAIsRepository = mockk()
    private val subject = GetDriveOwnerUseCase(repository)

    @Test
    fun `Given repository returns owner When invoke is called Then should return owner`() =
        runTest {
            val owner = Owner(
                name = "Rosemarie",
                email = "Joline",
                photoUrl = "Sherica"
            )
            every { repository.getOwner() } returns flowOf(owner)

            val result = subject()

            result.test {
                assertEquals(owner, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Given repository throws exception When invoke is called Then should propagate exception`() =
        runTest {
            val expectedMessage = "Test exception"
            val cause = Throwable(expectedMessage)
            every { repository.getOwner() } returns flow { throw cause }

            val result = assertFailsWith<Throwable> {
                subject().collect()
            }
            assertEquals(expectedMessage, result.message)
        }
}
