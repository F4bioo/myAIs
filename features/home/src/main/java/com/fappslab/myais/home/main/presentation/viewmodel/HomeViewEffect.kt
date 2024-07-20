package com.fappslab.myais.home.main.presentation.viewmodel

import com.fappslab.myais.arch.camerax.model.RatioType
import com.fappslab.myais.domain.model.SaveMemory

internal sealed interface HomeViewEffect {
    data object FlipCamera : HomeViewEffect
    data class NavigateToMemories(val ratioType: RatioType) : HomeViewEffect
    data class CheckAuthMemories(val ratioType: RatioType) : HomeViewEffect
    data class CheckAuthMemory(val saveMemory: SaveMemory) : HomeViewEffect
}
