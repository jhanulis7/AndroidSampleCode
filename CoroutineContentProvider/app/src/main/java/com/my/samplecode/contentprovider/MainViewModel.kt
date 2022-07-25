package com.my.samplecode.contentprovider

import android.content.ContentResolver
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: ContactsRepository
): ViewModel() {
    private var job: Job? = null
    var contactsList : List<Contact>  by mutableStateOf(listOf())

    fun fetchContacts(
        contentResolver: ContentResolver,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        job = viewModelScope.launch {
            Log.d("TEST", "fetchContacts")
            withContext(Dispatchers.Main) {
                contactsList = repository.fetchContacts(contentResolver, dispatcher)
                Log.d("TEST", "fetchContacts successful")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}