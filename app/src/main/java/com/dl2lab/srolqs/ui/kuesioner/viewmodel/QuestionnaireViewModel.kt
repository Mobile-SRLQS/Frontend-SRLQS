package com.dl2lab.srolqs.ui.kuesioner.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuestionnaireViewModel : ViewModel() {
    private val answers = MutableLiveData<HashMap<String, Int>>(HashMap())

    fun setAnswer(questionNumber: String, answer: Int) {
        val currentAnswers = answers.value ?: HashMap()
        currentAnswers[questionNumber] = answer
        answers.value = currentAnswers
    }

    fun getAnswers(): LiveData<HashMap<String, Int>> = answers

    fun logAnswers() {
        val currentAnswers = answers.value ?: HashMap()
        Log.d("QuestionnaireViewModel", "Current answers: $currentAnswers")
    }

    fun getAnswersArray(): Array<Int> {
        val sortedAnswers = answers.value?.toSortedMap(compareBy { it.toIntOrNull() ?: Int.MAX_VALUE }) ?: sortedMapOf()
        return sortedAnswers.values.toTypedArray()
    }

}