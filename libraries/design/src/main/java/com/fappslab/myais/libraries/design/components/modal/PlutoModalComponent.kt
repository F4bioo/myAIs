package com.fappslab.myais.libraries.design.components.modal

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.SecureFlagPolicy
import com.fappslab.myais.libraries.design.R
import com.fappslab.myais.libraries.design.accessibility.semantics
import com.fappslab.myais.libraries.design.components.button.ButtonType
import com.fappslab.myais.libraries.design.components.button.PlutoButtonComponent
import com.fappslab.myais.libraries.design.components.lorem.loremIpsum
import com.fappslab.myais.libraries.design.components.modal.model.ButtonModel
import com.fappslab.myais.libraries.design.theme.PlutoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlutoModalComponent(
    shouldShow: Boolean,
    isSheetSwipeEnabled: Boolean = true,
    onDismiss: () -> Unit = {},
    @StringRes titleRes: Int? = null,
    titleStr: String? = null,
    @StringRes messageRes: Int? = null,
    messageStr: String? = null,
    image: (@Composable () -> Unit)? = null,
    primaryButton: (ButtonModel.() -> Unit)? = null,
    secondaryButton: (ButtonModel.() -> Unit)? = null,
    tertiaryButton: (ButtonModel.() -> Unit)? = null,
    customContent: @Composable (() -> Unit)? = null,
) {
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { isSheetSwipeEnabled },
        skipPartiallyExpanded = true,
    )

    if (shouldShow) {
        ModalBottomSheet(
            properties = ModalBottomSheetProperties(
                securePolicy = SecureFlagPolicy.SecureOff,
                isFocusable = true,
                shouldDismissOnBackPress = isSheetSwipeEnabled
            ),
            onDismissRequest = {
                onDismiss()
            },
            sheetState = sheetState,
            shape = RoundedCornerShape(
                topStart = PlutoTheme.radius.large,
                topEnd = PlutoTheme.radius.large
            ),
            dragHandle = {
                PlutoDragHandleComponent(
                    isSheetSwipeEnabled = isSheetSwipeEnabled,
                    onClosed = {
                        onDismiss()
                    }
                )
            },
            scrimColor = Color.Black.copy(alpha = .5f)
        ) {
            ModalDefaultContent(
                isDragEnabled = isSheetSwipeEnabled,
                titleRes = titleRes,
                titleStr = titleStr,
                messageRes = messageRes,
                messageStr = messageStr,
                image = image,
                modalContent = customContent,
                primaryButton = primaryButton?.let { ButtonModel().apply(it) },
                secondaryButton = secondaryButton?.let { ButtonModel().apply(it) },
                tertiaryButton = tertiaryButton?.let { ButtonModel().apply(it) },
            )
        }
    }
}

@Composable
private fun ModalDefaultContent(
    modifier: Modifier = Modifier,
    isDragEnabled: Boolean,
    @StringRes titleRes: Int?,
    titleStr: String?,
    @StringRes messageRes: Int?,
    messageStr: String?,
    image: (@Composable () -> Unit)?,
    modalContent: @Composable (() -> Unit)?,
    primaryButton: ButtonModel?,
    secondaryButton: ButtonModel?,
    tertiaryButton: ButtonModel?,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(PlutoTheme.dimen.dp16)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .dragEnabled(isDragEnabled),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (titleRes != null || titleStr != null) {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            Text(
                modifier = Modifier.semantics {
                    heading()
                },
                text = titleRes?.let {
                    stringResource(id = it)
                } ?: titleStr.orEmpty(),
                style = PlutoTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
        if (messageRes != null || messageStr != null) {
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp8))
            Text(
                text = messageRes?.let {
                    stringResource(id = it)
                } ?: messageStr.orEmpty(),
                style = PlutoTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp12))
        }
        image?.let { block ->
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp12))
            block()
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp48))
        }
        modalContent?.let { block ->
            block()
        }
        primaryButton?.let { model ->
            PlutoButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = model.buttonTextRes?.let {
                    stringResource(id = it)
                } ?: model.buttonText.orEmpty(),
                buttonType = ButtonType.Primary,
                onClick = model.onCLicked
            )
        }
        secondaryButton?.let { model ->
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            PlutoButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = model.buttonTextRes?.let {
                    stringResource(id = it)
                } ?: model.buttonText.orEmpty(),
                buttonType = ButtonType.Secondary,
                onClick = model.onCLicked
            )
        }
        tertiaryButton?.let { model ->
            Spacer(modifier = Modifier.size(PlutoTheme.dimen.dp16))
            PlutoButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = model.buttonTextRes?.let {
                    stringResource(id = it)
                } ?: model.buttonText.orEmpty(),
                buttonType = ButtonType.Tertiary,
                onClick = model.onCLicked
            )
        }
    }
}

fun Modifier.dragEnabled(enabled: Boolean): Modifier {
    return pointerInput(enabled) {
        if (!enabled) {
            detectDragGestures { _, _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ModalDefaultContentPreview() {
    ModalDefaultContent(
        modifier = Modifier.padding(PlutoTheme.dimen.dp16),
        isDragEnabled = true,
        titleRes = null,
        titleStr = loremIpsum { 2 },
        messageRes = null,
        messageStr = loremIpsum { 10 },
        image = {
            Image(
                modifier = Modifier.size(PlutoTheme.dimen.dp64),
                painter = painterResource(id = R.drawable.illu_error_analyze),
                contentDescription = null
            )
        },
        modalContent = null,
        primaryButton = ButtonModel().apply {
            buttonText = "Primary Button"
            onCLicked = {}
        },
        secondaryButton = ButtonModel().apply {
            buttonText = "Secondary Button"
            onCLicked = {}
        },
        tertiaryButton = ButtonModel().apply {
            buttonText = "Tertiary Button"
            onCLicked = {}
        }
    )
}
