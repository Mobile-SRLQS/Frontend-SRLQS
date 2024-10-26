package com.dl2lab.srolqs.ui.kuesioner.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class QuestionnaireViewModel : ViewModel() {

    // Initialize a list with nulls for 24 questions
    private val _answers = MutableList<Int?>(24) { null }

    // Function to set an answer for a specific question number
    fun setAnswer(questionNumber: Int, answer: Int) {
        if (questionNumber in 1..24) {
            _answers[questionNumber - 1] = answer
            Log.d("QuestionnaireViewModel", "Set answer for question $questionNumber to $answer")
        }
    }

    // Retrieve all answers, replacing nulls with a default value if necessary
    fun getAnswers(): List<Int> {
        val answersList = _answers.map { it ?: 0 } // Replace null with 0 if unanswered
        Log.d("QuestionnaireViewModel", "Retrieved answers: $answersList")
        return answersList
    }

    // Function to get the answer for a specific question number
    fun getAnswer(questionNumber: Int): Int? {
        val answer = if (questionNumber in 1..24) _answers[questionNumber - 1] else null
        Log.d("QuestionnaireViewModel", "Get answer for question $questionNumber: $answer")
        return answer
    }
}
