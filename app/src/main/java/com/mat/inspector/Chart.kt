package com.mat.inspector

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chart(
    val name: String,
    val plots: Map<String, Int>,
    val samples: Int,
) : Parcelable