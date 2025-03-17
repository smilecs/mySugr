package com.smile.mysugr.di

import android.content.Context
import androidx.room.Room
import com.smile.logbook.LogDatabase
import com.smile.logbook.data.local.LogDao
import com.smile.logbook.domain.DefaultDispatcher
import com.smile.logbook.domain.IoDispatcher
import com.smile.logbook.domain.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Injector {
    @Provides
    @Singleton
    fun provideLogDatabase(@ApplicationContext context: Context): LogDatabase {
        return Room.databaseBuilder(
            context,
            LogDatabase::class.java,
            "database-logbook",
        ).build()
    }

    @Provides
    @Singleton
    fun provideLogDao(logDatabase: LogDatabase): LogDao {
        return logDatabase.logDao()
    }


    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
