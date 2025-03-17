package com.smile.logbook.domain

import kotlinx.coroutines.flow.Flow

interface LogRepository {
    suspend fun insertEntity(log: Log)

    suspend fun getAllEntries(): Flow<List<Log>>
}
