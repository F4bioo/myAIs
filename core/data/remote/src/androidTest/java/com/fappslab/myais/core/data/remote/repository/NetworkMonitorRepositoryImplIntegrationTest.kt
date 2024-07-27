package com.fappslab.myais.core.data.remote.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.fappslab.myais.core.data.remote.network.monitor.NetworkMonitor
import com.fappslab.myais.core.data.remote.network.monitor.NetworkMonitorImpl
import com.fappslab.myais.core.domain.model.NetworkStateType
import com.fappslab.myais.core.domain.repository.NetworkMonitorRepository
import com.fappslab.myais.libraries.arch.extension.isNotNull
import com.fappslab.myais.libraries.testing.rules.MainCoroutineTestRule
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
internal class NetworkMonitorRepositoryImplIntegrationTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineTestRule(StandardTestDispatcher())

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)
    private val networkCallback = slot<ConnectivityManager.NetworkCallback>()
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var subject: NetworkMonitorRepository

    @Before
    fun setUp() {
        every {
            connectivityManager.registerNetworkCallback(
                any(),
                capture(networkCallback)
            )
        } just Runs
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun given_onAvailable_When_watchNetworkState_is_invoked_Then_should_emit_status_type_Available() =
        runTest {
            // Given
            val expectedNetworkStateType = NetworkStateType.Available
            subject = setupSubject(expectedNetworkStateType)

            // When
            val result = subject.watchNetworkState()

            // Then
            result.test {
                networkCallback.captured.onAvailable(mockk())
                assertEquals(expectedNetworkStateType, awaitItem())
            }
        }

    @Test
    fun given_onUnavailable_When_watchNetworkState_is_invoked_Then_should_emit_status_type_Unavailable() =
        runTest {
            // Given
            val expectedNetworkStateType = NetworkStateType.Unavailable
            subject = setupSubject()

            // When
            val result = subject.watchNetworkState()

            // Then
            result.test {
                networkCallback.captured.onUnavailable()
                assertEquals(expectedNetworkStateType, awaitItem())
            }
        }

    @Test
    fun given_onLost_When_watchNetworkState_is_invoked_Then_should_emit_status_type_Lost() =
        runTest {
            // Given
            val expectedNetworkStateType = NetworkStateType.Lost
            subject = setupSubject()

            // When
            val result = subject.watchNetworkState()

            // Then
            result.test {
                networkCallback.captured.onLost(mockk())
                assertEquals(expectedNetworkStateType, awaitItem())
            }
        }

    @Test
    fun given_onCapabilitiesChanged_When_watchNetworkState_is_invoked_Then_should_emit_status_type_Available() =
        runTest {
            // Given
            val expectedNetworkStateType = NetworkStateType.Available
            val networkCapabilities = mockk<NetworkCapabilities>()
            subject = setupSubject(expectedNetworkStateType)

            // When
            val result = subject.watchNetworkState()

            // Then
            result.test {
                networkCallback.captured.onCapabilitiesChanged(mockk(), networkCapabilities)
                assertEquals(expectedNetworkStateType, awaitItem())
            }
        }

    private fun setupSubject(networkStateType: NetworkStateType? = null): NetworkMonitorRepository {
        val checkRealConnection: (ProducerScope<NetworkStateType>) -> Job = { scope ->
            networkStateType?.let {
                scope.trySend(it).isSuccess
            }
            Job()
        }

        networkMonitor = if (networkStateType.isNotNull()) {
            NetworkMonitorImpl(
                connectivityManager,
                mainCoroutineRule.testDispatcher,
                checkRealConnection
            )
        } else {
            NetworkMonitorImpl(
                connectivityManager,
                mainCoroutineRule.testDispatcher
            )
        }

        return NetworkMonitorRepositoryImpl(networkMonitor)
    }
}
