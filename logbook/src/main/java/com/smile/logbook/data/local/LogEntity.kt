package com.smile.logbook.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smile.logbook.domain.BgUnit

@Entity
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "bg_unit") val bgUnit: BgUnit,
    @ColumnInfo(name = "bg_value") val bgValue: Double,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)
