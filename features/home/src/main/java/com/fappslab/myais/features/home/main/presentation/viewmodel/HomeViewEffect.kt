package com.fappslab.myais.features.home.main.presentation.viewmodel

import com.fappslab.myais.libraries.arch.camerax.model.RatioType
import com.fappslab.myais.core.domain.model.SaveMemory

internal sealed interface HomeViewEffect {
    data class NavigateToMemories(val ratioType: RatioType) : HomeViewEffect
    data class CheckAuthMemories(val ratioType: RatioType) : HomeViewEffect
    data class CheckAuthMemory(val saveMemory: SaveMemory) : HomeViewEffect
}
