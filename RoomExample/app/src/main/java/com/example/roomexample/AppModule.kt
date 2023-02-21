package com.example.roomexample

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : ProductRoomDatabase {
        return ProductRoomDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideProductDao(productRoomDatabase: ProductRoomDatabase) : ProductDao =
        productRoomDatabase.productDao()
}

