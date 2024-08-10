package com.fappslab.myais.features.home.agreement.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.fappslab.myais.libraries.arch.simplepermission.model.PermissionStatus
import com.fappslab.myais.libraries.design.components.button.model.ButtonState
import com.fappslab.myais.libraries.testing.rules.MainCoroutineTestRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class AgreementRouteViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineTestRule(StandardTestDispatcher())

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val initialState = AgreementViewState()
    private lateinit var subject: AgreementViewModel

    @Before
    fun setUp() {
        subject = AgreementViewModel()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Given view model initialized When OnContinue intent received Then should navigate to home`() =
        runTest {
            // Given
            val expectedEffect = AgreementViewEffect.NavigateToHome

            // When
            subject.onViewIntent(AgreementViewIntent.OnContinue)

            // Then
            subject.effect.test {
                assertEquals(expectedEffect, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given view model initialized When OnOpenSettings intent received Then should navigate to settings`() {
        runTest {
            // Given
            val expectedEffect = AgreementViewEffect.NavigateToSettings

            // When
            subject.onViewIntent(AgreementViewIntent.OnOpenSettings)

            // Then
            subject.effect.test {
                assertEquals(expectedEffect, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Given view model initialized When OnPrivacyPolicy intent received Then should navigate to privacy policy`() {
        runTest {
            // Given
            val link = "some_link"
            val expectedEffect = AgreementViewEffect.NavigateToConditions(link)

            // When
            subject.onViewIntent(AgreementViewIntent.OnConditions(link))

            // Then
            subject.effect.test {
                assertEquals(expectedEffect, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `Given view model initialized When OnPermissionResult with Granted received Then should update state to granted permission`() {
        runTest {
            // Given
            val expectedFinalState = initialState.copy(
                isAlwaysDenied = false,
                isGrantedPermission = true,
                permissionStatus = PermissionStatus.Granted,
                buttonState = ButtonState.Enabled
            )

            // When
            subject.onViewIntent(AgreementViewIntent.OnPermissionResult(PermissionStatus.Granted))

            // Then
            subject.state.test {
                assertEquals(expectedFinalState, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `Given view model initialized When OnPermissionResult with AlwaysDenied received Then should update state to always denied`() {
        runTest {
            // Given
            val expectedFinalState = initialState.copy(isAlwaysDenied = true)

            // When
            subject.onViewIntent(AgreementViewIntent.OnPermissionResult(PermissionStatus.AlwaysDenied))

            // Then
            subject.state.test {
                assertEquals(expectedFinalState, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }
}
