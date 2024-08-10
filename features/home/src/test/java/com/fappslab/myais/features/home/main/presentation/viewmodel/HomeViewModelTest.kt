package com.fappslab.myais.features.home.main.presentation.viewmodel

import android.graphics.Bitmap
import android.util.Base64
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.fappslab.myais.core.data.remote.network.exception.model.HttpThrowable
import com.fappslab.myais.core.data.remote.network.exception.model.InternetThrowable
import com.fappslab.myais.core.domain.usecase.CreateContentUseCase
import com.fappslab.myais.core.domain.usecase.GetPromptUseCase
import com.fappslab.myais.core.domain.usecase.UploadDriveFileUseCase
import com.fappslab.myais.features.home.R
import com.fappslab.myais.features.home.main.presentation.model.AuthType
import com.fappslab.myais.features.home.main.presentation.model.FailureType
import com.fappslab.myais.features.home.main.presentation.model.FlashType
import com.fappslab.myais.features.home.main.presentation.model.MainStateType
import com.fappslab.myais.features.home.stub.base64StringStub
import com.fappslab.myais.features.home.stub.descriptionStub
import com.fappslab.myais.features.home.stub.memoryStub
import com.fappslab.myais.features.home.stub.promptTextStub
import com.fappslab.myais.features.home.stub.saveMemoryStub
import com.fappslab.myais.libraries.arch.camerax.model.RatioType
import com.fappslab.myais.libraries.testing.rules.MainCoroutineTestRule
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class HomeViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineTestRule(StandardTestDispatcher())

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val initialState = HomeViewState(FlashType.Off)
    private val getPromptUseCase = mockk<GetPromptUseCase>(relaxed = true)
    private val createContentUseCase = mockk<CreateContentUseCase>()
    private val uploadDriveFileUseCase = mockk<UploadDriveFileUseCase>(relaxed = true)
    private lateinit var subject: HomeViewModel

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Given OnCameraFlash intent When onViewIntent is invoked Then should expose state to FlashType On`() =
        runTest {
            // Given
            setupSubject(FlashType.Off)
            val expectedState = initialState.copy(flashType = FlashType.On)

            // When
            subject.onViewIntent(HomeViewIntent.OnCameraFlash)

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given OnCameraFlash intent When onViewIntent is invoked Then should expose state to FlashType Auto`() =
        runTest {
            // Given
            setupSubject(FlashType.On)
            val expectedState = initialState.copy(flashType = FlashType.Auto)

            // When
            subject.onViewIntent(HomeViewIntent.OnCameraFlash)

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given OnCameraFlash intent When onViewIntent is invoked Then should expose state to FlashType Off`() =
        runTest {
            // Given
            setupSubject(FlashType.Auto)
            val expectedState = initialState.copy(flashType = FlashType.Off)

            // When
            subject.onViewIntent(HomeViewIntent.OnCameraFlash)

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given OnNavigateToCamera intent When onViewIntent is invoked Then should expose expected state`() =
        runTest {
            // Given
            setupSubject()
            val expectedState = initialState.copy(mainStateType = MainStateType.Camera)

            // When
            subject.onViewIntent(HomeViewIntent.OnNavigateToCamera)

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given OnFailureModalClose intent When onViewIntent is invoked Then should expose expected state`() =
        runTest {
            // Given
            setupSubject()
            val expectedState = initialState.copy(shouldShowFailure = false)

            // When
            subject.onViewIntent(HomeViewIntent.OnFailureModalClose)

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given OnMemoriesAuthChecker intent When onViewIntent is invoked Then should expose expected state and effect`() =
        runTest {
            // Given
            setupSubject()
            val expectedState = initialState.copy(
                shouldShowFailure = false,
                authType = AuthType.NavigateToMemories
            )
            val expectedEffect = HomeViewEffect.MemoriesAuthManager(RatioType.RATIO_16_9)

            // When
            subject.onViewIntent(HomeViewIntent.OnMemoriesAuthChecker)

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            subject.effect.test {
                assertEquals(expectedEffect, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given OnSaveMemory intent When onViewIntent is invoked Then should expose expected failure state`() =
        runTest {
            // Given
            setupSubject()
            val expectedFirstState = initialState.copy(
                uploadDescription = R.string.home_desc_save_memory,
                shouldShowLoading = true,
            )
            val expectedFinalState = expectedFirstState.copy(
                mainStateType = MainStateType.Preview,
                failureType = FailureType.UploadErrorMemory,
                shouldShowLoading = false,
                shouldShowFailure = true,
            )
            every { uploadDriveFileUseCase(any()) } returns flow { throw Throwable() }

            // When
            subject.onViewIntent(HomeViewIntent.OnSaveMemory(saveMemoryStub))

            // Then
            subject.state.test {
                assertEquals(initialState, awaitItem())
                assertEquals(expectedFirstState, awaitItem())
                assertEquals(expectedFinalState, awaitItem())
            }
            verify { uploadDriveFileUseCase(any()) }
        }

    @Test
    fun `Given OnSaveMemory intent When onViewIntent is invoked Then should expose expected success state`() =
        runTest {
            // Given
            setupSubject()
            val expectedFirstState = initialState.copy(
                uploadDescription = R.string.home_desc_save_memory,
                shouldShowLoading = true,
            )
            val expectedFinalState = expectedFirstState.copy(
                uploadDescription = R.string.home_desc_uploaded,
                mainStateType = MainStateType.Preview,
                shouldShowLoading = false,
            )
            every { uploadDriveFileUseCase(any()) } returns flowOf(memoryStub)

            // When
            subject.onViewIntent(HomeViewIntent.OnSaveMemory(saveMemoryStub))

            // Then
            subject.state.test {
                assertEquals(initialState, awaitItem())
                assertEquals(expectedFirstState, awaitItem())
                assertEquals(expectedFinalState, awaitItem())
            }
            verify { uploadDriveFileUseCase(any()) }
        }

    @Test
    fun `Given OnTakePicture intent When onViewIntent is invoked Then should expose expected failure state with ConnectionErrorDescription type`() =
        runTest {
            // Given
            setupSubject()
            val cause = InternetThrowable()
            val imageBitmap = mockk<Bitmap>(relaxed = true)
            val expectedFirstState = initialState.copy(
                imageBitmap = imageBitmap
            )
            val expectedSecondState = expectedFirstState.copy(
                mainStateType = MainStateType.Analyze,
                shouldShowFailure = false
            )
            val expectedFinalState = expectedSecondState.copy(
                mainStateType = MainStateType.Camera,
                failureType = FailureType.ConnectionErrorDescription,
                shouldShowFailure = true,
            )
            every { getPromptUseCase(any()) } returns flowOf(promptTextStub)
            every { createContentUseCase(any()) } returns flow { throw cause }
            mockkStatic(Base64::class)
            every { Base64.encodeToString(any(), any()) } returns base64StringStub

            // When
            subject.onViewIntent(HomeViewIntent.OnTakePicture(imageBitmap))

            // Then
            subject.state.test {
                assertEquals(expectedFirstState, awaitItem())
                assertEquals(expectedSecondState, awaitItem())
                assertEquals(expectedFinalState, awaitItem())
            }
            verify { getPromptUseCase(any()) }
            verify { createContentUseCase(any()) }
            verify { Base64.encodeToString(any(), any()) }
        }

    @Test
    fun `Given OnTakePicture intent When onViewIntent is invoked Then should expose expected failure state with AnalyzeErrorDescription type`() =
        runTest {
            // Given
            setupSubject()
            val cause = mockk<HttpThrowable>(relaxed = true)
            val imageBitmap = mockk<Bitmap>(relaxed = true)
            val expectedFirstState = initialState.copy(
                imageBitmap = imageBitmap
            )
            val expectedSecondState = expectedFirstState.copy(
                mainStateType = MainStateType.Analyze,
                shouldShowFailure = false
            )
            val expectedFinalState = expectedSecondState.copy(
                mainStateType = MainStateType.Camera,
                failureType = FailureType.AnalyzeErrorDescription,
                shouldShowFailure = true,
            )
            every { getPromptUseCase(any()) } returns flowOf(promptTextStub)
            every { createContentUseCase(any()) } returns flow { throw cause }
            mockkStatic(Base64::class)
            every { Base64.encodeToString(any(), any()) } returns base64StringStub

            // When
            subject.onViewIntent(HomeViewIntent.OnTakePicture(imageBitmap))

            // Then
            subject.state.test {
                assertEquals(expectedFirstState, awaitItem())
                assertEquals(expectedSecondState, awaitItem())
                assertEquals(expectedFinalState, awaitItem())
            }
            verify { getPromptUseCase(any()) }
            verify { createContentUseCase(any()) }
            verify { Base64.encodeToString(any(), any()) }
        }

    @Test
    fun `Given OnTakePicture intent When onViewIntent is invoked Then should expose expected failure state with GenericErrorDescription type`() =
        runTest {
            // Given
            setupSubject()
            val cause = Throwable()
            val imageBitmap = mockk<Bitmap>(relaxed = true)
            val expectedFirstState = initialState.copy(
                imageBitmap = imageBitmap
            )
            val expectedSecondState = expectedFirstState.copy(
                mainStateType = MainStateType.Analyze,
                shouldShowFailure = false
            )
            val expectedFinalState = expectedSecondState.copy(
                mainStateType = MainStateType.Camera,
                failureType = FailureType.GenericErrorDescription,
                shouldShowFailure = true,
            )
            every { getPromptUseCase(any()) } returns flowOf(promptTextStub)
            every { createContentUseCase(any()) } returns flow { throw cause }
            mockkStatic(Base64::class)
            every { Base64.encodeToString(any(), any()) } returns base64StringStub

            // When
            subject.onViewIntent(HomeViewIntent.OnTakePicture(imageBitmap))

            // Then
            subject.state.test {
                assertEquals(expectedFirstState, awaitItem())
                assertEquals(expectedSecondState, awaitItem())
                assertEquals(expectedFinalState, awaitItem())
            }
            verify { getPromptUseCase(any()) }
            verify { createContentUseCase(any()) }
            verify { Base64.encodeToString(any(), any()) }
        }

    @Test
    fun `Given OnTakePicture intent When onViewIntent is invoked Then should expose expected success state`() =
        runTest {
            // Given
            setupSubject()
            val description = descriptionStub
            val imageBitmap = mockk<Bitmap>(relaxed = true)
            val expectedFirstState = initialState.copy(
                imageBitmap = imageBitmap
            )
            val expectedSecondState = expectedFirstState.copy(
                mainStateType = MainStateType.Analyze,
                shouldShowFailure = false
            )
            val expectedFinalState = expectedSecondState.copy(
                uploadDescription = R.string.home_desc_save_memory,
                imageDescription = description,
                mainStateType = MainStateType.Preview,
            )
            every { getPromptUseCase(any()) } returns flowOf(promptTextStub)
            every { createContentUseCase(any()) } returns flowOf(description)
            mockkStatic(Base64::class)
            every { Base64.encodeToString(any(), any()) } returns base64StringStub

            // When
            subject.onViewIntent(HomeViewIntent.OnTakePicture(imageBitmap))

            // Then
            subject.state.test {
                assertEquals(expectedFirstState, awaitItem())
                assertEquals(expectedSecondState, awaitItem())
                assertEquals(expectedFinalState, awaitItem())
            }
            verify { getPromptUseCase(any()) }
            verify { createContentUseCase(any()) }
            verify { Base64.encodeToString(any(), any()) }
        }

    @Test
    fun `Given OnGoogleAuthResulOk intent When onViewIntent is invoked Then should expose expected effect`() =
        runTest {
            // Given
            setupSubject()
            subject.onViewIntent(HomeViewIntent.OnMemoriesAuthChecker)
            val expectedEffect = HomeViewEffect.NavigateToMemories(RatioType.RATIO_16_9)

            // When
            subject.onViewIntent(HomeViewIntent.OnGoogleAuthResulOk)

            // Then
            subject.effect.test {
                assertEquals(expectedEffect, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given OnGoogleAuthResulOk intent When onViewIntent is invoked Then should expose expected failure state`() =
        runTest {
            // Given
            setupSubject()
            val saveMemory = saveMemoryStub
            subject.onViewIntent(HomeViewIntent.OnUploadAuthChecker(saveMemory))
            val expectedFirstState = initialState.copy(
                saveMemory = saveMemory,
                authType = AuthType.UploadMemory
            )
            val expectedSecondState = expectedFirstState.copy(
                uploadDescription = R.string.home_desc_save_memory,
                shouldShowLoading = true,
            )
            val expectedFinalState = expectedFirstState.copy(
                mainStateType = MainStateType.Preview,
                failureType = FailureType.UploadErrorMemory,
                shouldShowLoading = false,
                shouldShowFailure = true
            )
            every { uploadDriveFileUseCase(saveMemory) } returns flow { throw Throwable() }

            // When
            subject.onViewIntent(HomeViewIntent.OnGoogleAuthResulOk)

            // Then
            subject.state.test {
                assertEquals(expectedFirstState, awaitItem())
                assertEquals(expectedSecondState, awaitItem())
                assertEquals(expectedFinalState, awaitItem())
            }
        }

    @Test
    fun `Given OnGoogleAuthResulOk intent When onViewIntent is invoked Then should expose expected success state`() =
        runTest {
            // Given
            setupSubject()
            val saveMemory = saveMemoryStub
            subject.onViewIntent(HomeViewIntent.OnUploadAuthChecker(saveMemory))
            val expectedFirstState = initialState.copy(
                saveMemory = saveMemory,
                authType = AuthType.UploadMemory
            )
            val expectedSecondState = expectedFirstState.copy(
                uploadDescription = R.string.home_desc_save_memory,
                shouldShowLoading = true,
            )
            val expectedFinalState = expectedFirstState.copy(
                uploadDescription = R.string.home_desc_uploaded,
                mainStateType = MainStateType.Preview,
                shouldShowLoading = false,
            )
            every { uploadDriveFileUseCase(saveMemory) } returns flowOf(memoryStub)

            // When
            subject.onViewIntent(HomeViewIntent.OnGoogleAuthResulOk)

            // Then
            subject.state.test {
                assertEquals(expectedFirstState, awaitItem())
                assertEquals(expectedSecondState, awaitItem())
                assertEquals(expectedFinalState, awaitItem())
            }
        }

    @Test
    fun `Given OnGoogleAuthResulCanceled intent When onViewIntent is invoked Then should expose state`() =
        runTest {
            // Given
            setupSubject()
            val status = Status(CommonStatusCodes.NETWORK_ERROR, "Network error", null)
            val expectedState = initialState.copy(
                failureType = FailureType.ConnectionErrorMemories,
                shouldShowFailure = true
            )

            // When
            subject.onViewIntent(HomeViewIntent.OnGoogleAuthResulCanceled(status))

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
            }
        }

    @Test
    fun `Given OnBackHandler intent When onViewIntent is invoked Then should expose expected state with Preview type`() =
        runTest {
            // Given
            setupSubject()
            val mainStateType = MainStateType.Preview
            val expectedState = initialState.copy(mainStateType = MainStateType.Camera)

            // When
            subject.onViewIntent(HomeViewIntent.OnBackHandler(mainStateType))

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
            }
        }

    @Test
    fun `Given OnBackHandler intent When onViewIntent is invoked Then should expose expected state with Camera type`() =
        runTest {
            // Given
            setupSubject()
            val mainStateType = MainStateType.Camera

            // When
            subject.onViewIntent(HomeViewIntent.OnBackHandler(mainStateType))

            // Then
            subject.state.test {
                assertEquals(initialState, awaitItem())
            }
        }

    @Test
    fun `Given OnBackHandler intent When onViewIntent is invoked Then should expose expected state with Analyze type`() =
        runTest {
            // Given
            setupSubject()
            val mainStateType = MainStateType.Analyze

            // When
            subject.onViewIntent(HomeViewIntent.OnBackHandler(mainStateType))

            // Then
            subject.state.test {
                assertEquals(initialState, awaitItem())
            }
        }

    @Test
    fun `Given OnFailureCheckAuth intent When onViewIntent is invoked Then should expose expected failure state with ConnectionErrorDescription type`() =
        runTest {
            // Given
            setupSubject()
            val cause = InternetThrowable()
            val expectedState = initialState.copy(
                mainStateType = MainStateType.Camera,
                failureType = FailureType.ConnectionErrorDescription,
                shouldShowFailure = true,
            )

            // When
            subject.onViewIntent(HomeViewIntent.OnFailureCheckAuth(cause))

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
            }
        }

    @Test
    fun `Given OnFailureCheckAuth intent When onViewIntent is invoked Then should expose expected failure state with AnalyzeErrorDescription type`() =
        runTest {
            // Given
            setupSubject()
            val cause = mockk<HttpThrowable>(relaxed = true)
            val expectedState = initialState.copy(
                mainStateType = MainStateType.Camera,
                failureType = FailureType.AnalyzeErrorDescription,
                shouldShowFailure = true,
            )

            // When
            subject.onViewIntent(HomeViewIntent.OnFailureCheckAuth(cause))

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
            }
        }

    @Test
    fun `Given OnFailureCheckAuth intent When onViewIntent is invoked Then should expose expected failure state with GenericErrorDescription type`() =
        runTest {
            // Given
            setupSubject()
            val cause = Throwable()
            val expectedState = initialState.copy(
                mainStateType = MainStateType.Camera,
                failureType = FailureType.GenericErrorDescription,
                shouldShowFailure = true,
            )

            // When
            subject.onViewIntent(HomeViewIntent.OnFailureCheckAuth(cause))

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
            }
        }

    @Test
    fun `Given OnUploadAuthChecker intent When onViewIntent is invoked Then should expose expected state and effect`() =
        runTest {
            // Given
            setupSubject()
            val saveMemory = saveMemoryStub
            val expectedState = initialState.copy(
                saveMemory = saveMemory,
                authType = AuthType.UploadMemory
            )
            val expectedEffect = HomeViewEffect.UploadAuthManager(saveMemory)

            // When
            subject.onViewIntent(HomeViewIntent.OnUploadAuthChecker(saveMemory))

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
            }
            subject.effect.test {
                assertEquals(expectedEffect, awaitItem())
            }
        }

    @Test
    fun `Given OnFailureModalRetry intent When onViewIntent is invoked Then should try invoke getDescription again`() =
        runTest {
            // Given
            setupSubject()
            val imageBitmap = mockk<Bitmap>(relaxed = true)
            val failureType = listOf(
                FailureType.GenericErrorDescription,
                FailureType.ConnectionErrorDescription,
                FailureType.AnalyzeErrorDescription,
            ).random()
            mockkStatic(Base64::class)
            every { Base64.encodeToString(any(), any()) } returns base64StringStub
            subject.onViewIntent(HomeViewIntent.OnTakePicture(imageBitmap))

            // When
            subject.onViewIntent(HomeViewIntent.OnFailureModalRetry(failureType))

            // Then
            verify { Base64.encodeToString(any(), any()) }
        }

    @Test
    fun `Given OnFailureModalRetry intent When onViewIntent is invoked Then should expose expected state and effect`() =
        runTest {
            // Given
            setupSubject()
            val failureType = FailureType.ConnectionErrorMemories
            val expectedState = initialState.copy(
                shouldShowFailure = false,
                authType = AuthType.NavigateToMemories
            )
            val expectedEffect = HomeViewEffect.MemoriesAuthManager(RatioType.RATIO_16_9)

            // When
            subject.onViewIntent(HomeViewIntent.OnFailureModalRetry(failureType))

            // Then
            subject.state.test {
                assertEquals(expectedState, awaitItem())
            }
            subject.effect.test {
                assertEquals(expectedEffect, awaitItem())
            }
        }

    private fun setupSubject(flashType: FlashType = FlashType.Off) {
        subject = HomeViewModel(
            flashType = flashType,
            getPromptUseCase = getPromptUseCase,
            createContentUseCase = createContentUseCase,
            uploadDriveFileUseCase = uploadDriveFileUseCase,
            dispatcher = mainCoroutineRule.testDispatcher
        )
    }
}
