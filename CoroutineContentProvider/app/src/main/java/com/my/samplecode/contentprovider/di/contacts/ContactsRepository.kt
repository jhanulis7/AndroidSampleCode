package com.my.samplecode.contentprovider.di.contacts

import com.my.samplecode.contentprovider.model.Contact
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepository @Inject constructor(
    private val source: ContactsDataSource,
    private val sourceBt: ContactsBtDataSource
) {
    private fun <T>println(msg: T) = kotlin.io.println("[${Thread.currentThread().name}] $msg")

    //CursorLoader 클래스를 사용, 여기서는 코루틴 사용
    suspend fun fetchContacts(dispatcher: CoroutineDispatcher): List<Contact> =
        withContext(dispatcher) {
            println("ContactsRepository:fetchContacts")
            source.fetchContacts()
        }

    suspend fun fetchBtContacts(dispatcher: CoroutineDispatcher): List<Contact> =
        withContext(dispatcher) {
            println("ContactsRepository:fetchBtContacts")
            sourceBt.fetchContacts()
        }
}