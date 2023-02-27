package com.ftd.ivi.appstore.common.database.di

import android.content.Context
import com.ftd.ivi.appstore.common.database.AppDatabase
import com.ftd.ivi.appstore.common.database.dao.DownloadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideDownloadDao(appDatabase: AppDatabase): DownloadDao =
        appDatabase.downloadDao()
}
