package com.smile.logbook.data.local

import com.smile.logbook.domain.Log
import java.util.Date

object LogMapper {

    fun fromLogEntities(entities: List<LogEntity>): List<Log> =
        entities.map {
            fromLogEntity(it)
        }

    fun toLogEntity(log: Log) =
        LogEntity(bgUnit = log.bg, bgValue = log.value, createdAt = Date().time)

    private fun fromLogEntity(logEntity: LogEntity): Log =
        Log(bg = logEntity.bgUnit, value = logEntity.bgValue)
}
