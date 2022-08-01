package com.my.samplecode.contentprovider.di.message

import com.my.samplecode.contentprovider.model.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    private val source: MessagesDataSource
) {
    private fun <T>println(msg: T) = kotlin.io.println("[${Thread.currentThread().name}] $msg")

    //CursorLoader 클래스를 사용, 여기서는 코루틴 사용
    suspend fun fetchMessages(dispatcher: CoroutineDispatcher): List<Message> =
        withContext(dispatcher) {
            println("MessagesRepository:fetchMessages")
            source.fetchMessages()
        }
}