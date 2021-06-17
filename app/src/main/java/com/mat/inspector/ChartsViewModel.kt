package com.mat.inspector

import androidx.lifecycle.ViewModel

class ChartsViewModel : ViewModel() {

    data class ChartWithData(
        val chart: Configuration.Chart,
        val data: List<Double>,
    )
}