package com.my.sample.algorithmmatrial3.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

// https://blog.canopas.com/7-useful-ways-to-create-flow-in-kotlin-577992b73315
class Flow1Quiz @Inject constructor() {
    /**
     * Flow1quiz flowOf( …)
     * "chulsu", "kildong", "dongsu" emit 하고 collect 하여 프린트하는 suspend func
     * @constructor Create empty Flow1quiz
     */
    suspend fun quizOne() {
        val name = flowOf("chulsu", "kildong", "dongsu")
        name.collect { name ->
            println("name: $name")
        }
    }

    /**
     * Quiz two asFlow()
     * "chulsu", "kildong", "dongsu" 를 가진 list 를 이용 하여 flow 구현
     */
    suspend fun quizTwo() {
        val name = listOf("chulsu", "kildong", "dongsu").asFlow()
        name.collect { name ->
            println("name: $name")
        }
    }

    /**
     * Quiz Three flow{ } cold stream
     * "chulsu", "kildong", "dongsu" 를 가진 list 를 이용 하여 flow 구현
     * cold stream 이므로, emit 이 다 했지만 collect 후에 처음부터 받는다.
     */
    suspend fun quizThree() {
        val names = listOf("chulsu", "kildong", "dongsu")
        val nameInput = flow {
            for (name in names) {
                emit(name)
            }
        }
        delay(1000)
        nameInput.collect { name ->
            println("name: $name")
        }
    }

    /**
     * Quiz four : MutableSharedFlow 출력 문제
     * Output
     * Job1: Collected User(name=User 2)
     * Job2: Collected User(name=User 2)
     * hot stream 이므로 collect 시 받으나, shared flow replay = 1 이므로 이전꺼 1개 받음음     */
    fun quizFour() = runBlocking {
        val userFlow = MutableSharedFlow<String>(replay = 1)

        userFlow.tryEmit("User 0")
        userFlow.tryEmit("User 1")
        userFlow.tryEmit("User 2")

        val job1 = userFlow.onEach {
            println("Collected $it")
        }.launchIn(this)

        val job2 = userFlow.onEach {
            println("Collected $it")
        }.launchIn(this)

        delay(1000)
        job1.cancel()
        delay(1000)
        job2.cancel()
    }

    /**
     * Quiz five : normal flow vs channel flow
     * normal flow Output
     *  emit : 1
     *  collect 1
     *  5 sec delay
     *  emit : 2
     *  collect 2
     */
    fun quizFive() {
        val scope = CoroutineScope(Dispatchers.IO)
        val flow = flow<String> {
            println("emit : 1")
            emit("1")
            println("emit : 2")
            emit("2")
        }

        scope.launch {
            flow.collect {
                println("collect $it")
                delay(5000)
            }
        }
    }

    /**
     * Quiz six : normal flow vs channel flow
     * channel flow Output
     *  emit : 1
     *  collect 1
     *  5 sec delay
     *  emit : 2
     *  collect 2
     */
    fun quizSix() {
        val scope = CoroutineScope(Dispatchers.IO)
        val flow = flow<String> {
            println("emit : 1")
            emit("1")
            println("emit : 2")
            emit("2")
        }

        scope.launch {
            flow.collect {
                println("collect $it")
                delay(5000)
            }
        }
    }
}
