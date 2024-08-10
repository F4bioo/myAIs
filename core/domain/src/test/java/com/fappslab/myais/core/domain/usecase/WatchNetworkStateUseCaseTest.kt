package com.fappslab.myais.core.domain.usecase

import app.cash.turbine.test
import com.fappslab.myais.core.domain.model.NetworkStateType
import com.fappslab.myais.core.domain.repository.NetworkMonitorRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class WatchNetworkStateUseCaseTest {

    private val repository: NetworkMonitorRepository = mockk()
    private val subject = WatchNetworkStateUseCase(repository)

    @Test
    fun `Given repository returns connected state When invoke is called Then should return Available type`() =
        runTest {
            val expectedState = NetworkStateType.Available
            every { repository.watchNetworkState() } returns flowOf(expectedState)

            val result = subject()

            result.test {
                assertEquals(expectedState, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Given repository returns disconnected state When invoke is called Then should return Unavailable type`() =
        runTest {
            val expectedState = NetworkStateType.Unavailable
            every { repository.watchNetworkState() } returns flowOf(expectedState)

            val result = subject()

            result.test {
                assertEquals(expectedState, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Given repository returns disconnected state When invoke is called Then should return Lost type`() =
        runTest {
            val expectedState = NetworkStateType.Lost
            every { repository.watchNetworkState() } returns flowOf(expectedState)

            val result = subject()

            result.test {
                assertEquals(expectedState, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Given repository throws exception When invoke is called Then should propagate exception`() =
        runTest {
            val expectedMessage = "Test exception"
            val cause = Throwable(expectedMessage)
            every { repository.watchNetworkState() } returns flow { throw cause }

            val result = assertFailsWith<Throwable> {
                subject().collect()
            }
            assertEquals(expectedMessage, result.message)
        }
}
