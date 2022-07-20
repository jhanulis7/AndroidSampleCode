package com.my.samplecode.composeviewmodel.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.my.samplecode.composeviewmodel.model.UIState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyViewModel @Inject constructor() : ViewModel() {
    private val _state = mutableStateOf(UIState())
    val state: State<UIState> = _state

    sealed class UIEvent {
        object IncrementCounter: UIEvent()
        data class ChooseButton(val number: Int) : UIEvent()
    }

    fun onEvent(event: UIEvent) {
        when (event) {
            UIEvent.IncrementCounter -> {
                _state.value = state.value.copy(
                    counter = state.value.counter + 1
                )
            }
            is UIEvent.ChooseButton -> {
                _state.value = state.value.copy(
                    buttonNumber = event.number
                )
            }
        }
    }

    /**
     * mutates the state as per the user interaction.
     */
    fun onEvent2(event: UIEvent) {
        when (event) {
            UIEvent.IncrementCounter ->
                _state.value = state.value.copy(
                    counter = state.value.counter + 1
                )

            is UIEvent.ChooseButton ->
                _state.value = state.value.copy(
                    buttonNumber = event.number
                )
        }
    }

}