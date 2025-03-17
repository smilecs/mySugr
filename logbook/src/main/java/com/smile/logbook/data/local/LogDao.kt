package com.smile.logbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Insert
    suspend fun insertLog(logEntity: LogEntity)

    @Query("SELECT * FROM LogEntity")
    fun getAllLogs(): Flow<List<LogEntity>>
}
