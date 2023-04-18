package com.my.sample.algorithmmatrial3.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel 이라고 생각하고 UI State 를 하나 만들고,
 * MutableStateFlow 사용 해서 network fetch 를 통해서  _ui state
 */
@HiltViewModel
class FlowViewModelQuiz(private val showRepository: ShowRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Success(emptyList()))
    val uiState = _uiState.asStateFlow()

    fun fetchQuiz() {
        viewModelScope.launch {
            _uiState.tryEmit(UiState.Loading)
            showRepository.fetchUpcomingShow().collect { shows ->
                _uiState.tryEmit(UiState.Success(shows))
            }
        }
    }

}

data class Show(val image: String)

sealed class UiState {
    object Loading : UiState()
    data class Success(val shows: List<Show>) : UiState()
}

class ShowRepository @Inject constructor() {
    fun fetchUpcomingShow(): Flow<List<Show>> {
        println("fetchUpcomingShow()")
        return flowOf(listOf( Show("abc"), Show("def")))
    }
}