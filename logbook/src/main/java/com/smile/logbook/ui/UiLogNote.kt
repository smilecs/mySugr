package com.smile.logbook.ui

import androidx.compose.runtime.Immutable
import com.smile.logbook.domain.BgUnit

@Immutable
data class UiLogNote(
    val averageValue: String?,
    val bgCheckBox: List<BgUnit>,
    val bgValues: List<Double>
)
