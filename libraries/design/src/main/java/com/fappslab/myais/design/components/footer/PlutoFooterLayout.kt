package com.fappslab.myais.design.components.footer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.fappslab.myais.design.components.button.ButtonType
import com.fappslab.myais.design.components.button.PlutoButtonComponent
import com.fappslab.myais.design.theme.PlutoTheme

@Composable
fun PlutoFooterLayout(
    content: @Composable ColumnScope.() -> Unit
) {
    val color = MaterialTheme.colorScheme.onBackground
    val height = PlutoTheme.dimen.dp4

    Column {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            val gradient = Brush.verticalGradient(
                colors = listOf(color.copy(alpha = 0.02f), Color.Transparent),
                startY = size.height,
                endY = 0f
            )
            drawRect(brush = gradient)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = height,
                    start = PlutoTheme.dimen.dp16,
                    end = PlutoTheme.dimen.dp16,
                    bottom = PlutoTheme.dimen.dp16
                )
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlutoFooterLayoutPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        PlutoFooterLayout {
            PlutoButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = LoremIpsum(2).values.first(),
                buttonType = ButtonType.Primary
            )
            Spacer(modifier = Modifier.padding(PlutoTheme.dimen.dp8))
            PlutoButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = LoremIpsum(2).values.first(),
                buttonType = ButtonType.Secondary
            )
        }
    }
}
