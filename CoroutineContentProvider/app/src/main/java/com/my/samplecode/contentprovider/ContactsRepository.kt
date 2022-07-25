package com.my.samplecode.contentprovider

import android.content.ContentResolver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepository @Inject constructor(
    private val source: ContactsDataSource
) {
    //CursorLoader 클래스를 사용, 여기서는 코루틴 사용
    suspend fun fetchContacts(
        contentResolver: ContentResolver,
        dispatcher: CoroutineDispatcher
    ): List<Contact> =
        withContext(dispatcher) {
            source.fetchContacts(contentResolver)
        }
}