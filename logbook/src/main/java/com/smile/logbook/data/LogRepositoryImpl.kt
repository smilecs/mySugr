package com.smile.logbook.data

import com.smile.logbook.data.local.LogDao
import com.smile.logbook.data.local.LogMapper
import com.smile.logbook.domain.Log
import com.smile.logbook.domain.LogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(private val dao: LogDao) : LogRepository {
    override suspend fun insertEntity(log: Log) {
        val entity = LogMapper.toLogEntity(log)
        dao.insertLog(entity)
    }

    override suspend fun getAllEntries(): Flow<List<Log>> {
        return dao.getAllLogs().flatMapConcat {
            flowOf(LogMapper.fromLogEntities(it))
        }
    }
}
