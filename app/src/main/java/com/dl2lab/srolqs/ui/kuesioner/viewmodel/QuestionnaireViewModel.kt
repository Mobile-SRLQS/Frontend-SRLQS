package com.dl2lab.srolqs.ui.kuesioner.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.request.SubmitQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.SubmitQuestionnaireResponse
import com.dl2lab.srolqs.data.repository.SecuredRepository
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireViewModel(private val repository: SecuredRepository) : ViewModel() {
    private val answers = MutableLiveData<HashMap<String, Int>>(HashMap())
    private val classId = MutableLiveData<String>()
    private val period = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun setClassId(classId: String) {
        this.classId.value = classId
    }

    fun getClassId(): LiveData<String> = classId

    fun setPeriod(period: String) {
        this.period.value = period
    }

    fun getPeriod(): LiveData<String> = period

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

    fun getAnswerList(): List<Int> {
        val sortedAnswers = answers.value?.toSortedMap(compareBy { it.toIntOrNull() ?: Int.MAX_VALUE }) ?: sortedMapOf()
        return sortedAnswers.values.toList()
    }

    fun submitQuestionnaire(
        period: String,
        classId: String,
        questions: List<Int>
    ): LiveData<Response<SubmitQuestionnaireResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<SubmitQuestionnaireResponse>>()
        _isLoading.value = true
        val request = SubmitQuestionnaireRequest(period, classId, questions)
        val client = repository.submitQuestionnaire(request)
        client.enqueue(object : Callback<SubmitQuestionnaireResponse> {
            override fun onResponse(
                call: Call<SubmitQuestionnaireResponse>,
                response: Response<SubmitQuestionnaireResponse>
            ) {
                _isLoading.value = false
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<SubmitQuestionnaireResponse>, t: Throwable) {
                _isLoading.value = false
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<SubmitQuestionnaireResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message
            }
        })
        emitSource(responseLiveData)
    }





}