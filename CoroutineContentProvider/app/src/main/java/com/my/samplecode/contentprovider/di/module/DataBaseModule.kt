package com.my.samplecode.contentprovider.di.module

import android.content.Context
import com.my.samplecode.contentprovider.di.contacts.ContactsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Singleton
    @Provides
    fun provideContactsProvider(@ApplicationContext context: Context): ContactsDataSource {
        return ContactsDataSource(context.contentResolver)
    }

    //todo: Message
//    @Singleton
//    @Provides
//    fun provideMessagesProvider(application: Application): MessagesSource {
//        return MessagesSource(application.contentResolver)
//    }
}