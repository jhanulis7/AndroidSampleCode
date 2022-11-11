package com.my.sample.algorithmmatrial3.ui.manager

import com.my.sample.algorithmmatrial3.quiz.Level1Quiz
import com.my.sample.algorithmmatrial3.quiz.Level2Quiz
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizManager @Inject constructor() {
    @Inject
    lateinit var level1Quiz: Level1Quiz
    @Inject
    lateinit var level2Quiz: Level2Quiz

    fun getMap() : Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["알고리즘1"] = "2개의 숫자를 선택해서 최대값을 구하세요."
        map["알고리즘2"] = "2개의 숫자를 선택해서 최대값을 구하세요."
        map["알고리즘3"] = "2개의 숫자를 선택해서 최대값을 구하세요."
        map["알고리즘4"] = "2개의 숫자를 선택해서 최대값을 구하세요."
        map["알고리즘5"] = "2개의 숫자를 선택해서 최대값을 구하세요."

        return map
    }

    fun quizLevelOneDashOne() {
        level1Quiz.quizOne()
    }

    fun quizLevelOneDashTwo() {
        level1Quiz.quizTwo()
    }

    fun quizLevelTwoDashOne() {
        level2Quiz.quizTwo()
    }

    fun quizLevelTwoDashTwo() {
        level2Quiz.quizTwo()
    }
}