package com.smile.logbook.domain

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val MMOL = "mmoL/L"
const val MG = "mg/dL"

@Serializable
@Polymorphic
sealed class BgUnit {

    abstract val unit: String

    @Serializable
    @SerialName("mmoL")
    data object MmoL : BgUnit() {
        override val unit: String = MMOL
    }

    @Serializable
    @SerialName("mg")
    data object Mg : BgUnit() {
        override val unit: String = MG
    }
}
