package com.my.sample.algorithmmatrial3.quiz

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Level1Quiz @Inject constructor() {
    /**
     * 5 개의 입력(1, 5, 9, 3, 5)이 주어 졌을 때
     * 서로 다른 2개의 수를 뽑아서 더한 값 중에서 최대값을 구하라
     *
     * Input : [1, 5, 9, 3, 5]
     * Output : 14
     */

    fun quizOne() {
        val input = listOf(1, 5, 9, 3, 5)
        var max = input[0]
        for(i in input.indices) {
            for (j in i + 1 until input.size) {
                val temp = input[i] + input[j]
                if (temp > max) max = temp
            }
        }
        println("정답: $max")
    }

    /**
     * Quiz2
     * [1, 5, 2, 6, 3, 7, 4] 배열에서
     * 2번째 인덱스 부터 5번째 인덱스까지의 원소들을 추출한 새로운 배열에서 2번째 값 구하라
     */
    fun quizTwo() {
        val input = listOf(1, 5, 2, 6, 3, 7, 4)
        val output = input.subList(1, 5)
        println("정답: ${output[1]}")
    }

    /**
     * 주어진 [3, 7, 21, 10, 9, 11, 15] 배열에서 3으로 나누어 떨어지는 원소들을 오름차순으로 배열 하기
     *
     * Input : [3, 7, 21, 10, 9, 11, 15]
     * Output : [3, 9, 15, 21]
     */
    fun quizThree() {
        val input = listOf(3, 7, 21, 10, 9, 11, 15)
        val output = mutableListOf<Int>()
        input.forEach { element ->
            if (element % 3 == 0) {
                output.add(element)
            }
        }
        output.sort()
        println("정답: $output")
    }
}