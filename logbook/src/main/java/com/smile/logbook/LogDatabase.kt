package com.smile.logbook

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smile.logbook.data.local.BgUnitConverter
import com.smile.logbook.data.local.LogDao
import com.smile.logbook.data.local.LogEntity

@Database(entities = [LogEntity::class], version = 1)
@TypeConverters(BgUnitConverter::class)
abstract class LogDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
}
