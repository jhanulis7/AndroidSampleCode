package com.my.sample.algorithmmatrial3.quiz

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Level2Quiz @Inject constructor() {
    /**
     * 십진수 12를 2진수를 변환한뒤 변환한 수를 역순으로 재배열 하고 재배열된 수를 10진수로 반환하라.
     *
     * Input : 12
     * Output : 3
     * ex) 십진수12 -> 2진수(1100) -> 앞뒤반전(0011) -> 10진수 값 구하기(3)
     */
    fun quizOne() {
        val input = 12
        val output = input.toString(2).reversed().toInt(2)
        println("정답: $output")
    }

    /**
     * Quiz2_2
     * 주어진 숫자 중 3개의 수를 더했을 때 소수가 되는 경우의 개수를 구하려고 합니다. 숫자들이 들어있는 배열 nums가 매개변수로 주어질 때,
     * nums 에 있는 숫자들 중 서로 다른 3개를 골라 더했을 때 소수가 되는 경우의 개수를 return 하도록 solution 함수를 완성해주세요.
     * ex1 [1,2,3,4]	1
     * ex2 [1,2,7,6,4]	4
     * 입출력 예 #1
     * [1,2,4]를 이용해서 7을 만들 수 있습니다.
     * 입출력 예 #2
     * [1,2,4]를 이용해서 7을 만들 수 있습니다.
     * [1,4,6]을 이용해서 11을 만들 수 있습니다.
     * [2,4,7]을 이용해서 13을 만들 수 있습니다.
     * [4,6,7]을 이용해서 17을 만들 수 있습니다.
     * @return
     */
    fun quizTwo() {
        val input = listOf(1,2,7,6,4)
        var output = 0

        for (i in 0..input.size - 3) {
            for (j in i + 1..input.size - 2) {
                for (k in j + 1 until input.size) {
                    val sum = input[i] + input[j] + input[k]

                    if (isNonPrimeNumber(sum)) continue

                    output++
                }
            }
        }
        println("정답: $output")
    }

    private fun isNonPrimeNumber(number: Int): Boolean {
        for (i in 2 until number) {
            if (number % i == 0) return true
        }
        return false
    }
}