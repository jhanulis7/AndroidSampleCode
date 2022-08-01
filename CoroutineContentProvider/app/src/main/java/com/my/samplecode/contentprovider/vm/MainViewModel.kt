package com.my.samplecode.contentprovider.vm

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.samplecode.contentprovider.di.contacts.ContactsRepository
import com.my.samplecode.contentprovider.di.message.MessagesRepository
import com.my.samplecode.contentprovider.model.Contact
import com.my.samplecode.contentprovider.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val contactsRepository: ContactsRepository,
    private val messageRepository: MessagesRepository,
): ViewModel() {
    private var job: Job? = null
    var contactsList : List<Contact>  by mutableStateOf(listOf())
    var messagesList : List<Message>  by mutableStateOf(listOf())

    private fun <T>println(msg: T) = kotlin.io.println("[${Thread.currentThread().name}] $msg")

    fun fetchContacts(dispatcher: CoroutineDispatcher) {
        job = viewModelScope.launch {
            println("fetchContacts")
            //fetchContacts 에서 Dispatchers.IO workThread 로 돌아온 뒤 상위 MainThread 로 돌아옴..withContext 할 필요없음
            //withContext(Dispatchers.Main) {
                contactsList = contactsRepository.fetchContacts(dispatcher)
            println( "fetchContacts successful contactsList:$contactsList")
            //}
        }
    }

    fun fetchBtContacts(dispatcher: CoroutineDispatcher) {
        job = viewModelScope.launch {
            println("fetchBtContacts")
            contactsList = contactsRepository.fetchBtContacts(dispatcher)
            println( "fetchBtContacts successful contactsList:$contactsList")
        }
    }

    fun fetchMessages(dispatcher: CoroutineDispatcher) {
        job = viewModelScope.launch {
            println("fetchMessages start")
            messagesList = messageRepository.fetchMessages(dispatcher)
            println( "fetchMessages successful messagesList:$messagesList")
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}