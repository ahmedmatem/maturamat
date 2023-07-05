package com.ahmedmatem.android.matura.ui.test2

import com.ahmedmatem.android.matura.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SolutionsReviewViewModel : BaseViewModel() {
    private val _zoomScaleState: MutableStateFlow<Float> = MutableStateFlow(1f) // no zoom
    val zoomScaleState = _zoomScaleState.asStateFlow()

    fun onZoomScaleChanged(scale: Float) {
        _zoomScaleState.value = scale
    }
}