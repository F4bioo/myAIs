package com.fappslab.myais.design.components.lorem

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

fun loremIpsum(words: () -> Int): String {
    return LoremIpsum(words()).values.first()
}
