package com.my.sample.algorithmmatrial3

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    /**
     * 5 개의 입력(1, 5, 9, 3, 5)이 주어 졌을 때
     * 서로 다른 2개의 수를 뽑아서 더한 값 중에서 최대값을 구하라
     *
     * Input : [1, 5, 9, 3, 5]
     * Output : 14
     */
    @Test
    fun quiz1_1() {
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
    @Test
    fun quiz1_2() {
        val input = listOf(1, 5, 2, 6, 3, 7, 4)
        val output = input.subList(1, 5)
        println("정답: ${output[1]}")
    }

    @Test
    fun quiz1_3() {
        val input = listOf(1, 5, 2, 6, 3, 7, 4)
        val output = input.subList(1, 4)
        println("정답: ${output[1]}")
    }

    /**
     * 주어진 [3, 7, 21, 10, 9, 11, 15] 배열에서 3으로 나누어 떨어지는 원소들을 오름차순으로 배열 하기
     *
     * Input : [3, 7, 21, 10, 9, 11, 15]
     * Output : [3, 9, 15, 21]
     */
    @Test
    fun quiz1_4() {
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

    /**
     * 십진수 12를 2진수를 변환한뒤 변환한 수를 역순으로 재배열 하고 재배열된 수를 10진수로 반환하라.
     *
     * Input : 12
     * Output : 3
     * ex) 십진수12 -> 2진수(1100) -> 앞뒤반전(0011) -> 10진수 값 구하기(3)
     */
    @Test
    fun quiz2_1() {
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
    @Test
    fun quiz2_2() {
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