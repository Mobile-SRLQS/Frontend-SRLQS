package com.dl2lab.srolqs.ui.kuesioner.viewmodel

import android.util.Log
import androidx.annotation.Dimension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dl2lab.srolqs.data.remote.request.GetQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.request.JoinClassRequest
import com.dl2lab.srolqs.data.remote.request.SubmitQuestionnaireRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.DimensionReccomendation
import com.dl2lab.srolqs.data.remote.response.GetQuestionnaireResponse
import com.dl2lab.srolqs.data.remote.response.ProgressData
import com.dl2lab.srolqs.data.remote.response.ShowAvailablePeriodResponse
import com.dl2lab.srolqs.data.remote.response.StudentProgressResponse
import com.dl2lab.srolqs.data.remote.response.SubmitQuestionnaireResponse
import com.dl2lab.srolqs.data.repository.SecuredRepository
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireViewModel(private val repository: SecuredRepository) : ViewModel() {
    private val answers = MutableLiveData<HashMap<String, Int>>(HashMap())
    private val classId = MutableLiveData<String>()
    private val period = MutableLiveData<String>()


    private val _reccomendationText = MutableLiveData<String>()
    val reccomendationText: LiveData<String> = _reccomendationText


    private val _scoreResult = MutableLiveData<List<Float>>()
    val scoreResult: LiveData<List<Float>> = _scoreResult

    private val _scoreAverage = MutableLiveData<List<Float>>()
    val scoreAverage: LiveData<List<Float>> = _scoreAverage


    private val _reccomendation = MutableLiveData<DimensionReccomendation>()
    val reccomendation: LiveData<DimensionReccomendation> = _reccomendation

    private val _periods = MutableLiveData<List<String>>()
    val periods : LiveData<List<String>> = _periods

    private val _studentProgress = MutableLiveData<ProgressData>()
    val studentProgress : LiveData<ProgressData> = _studentProgress

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
        answers.value = currentAnswers
    }

    fun getAnswers(): LiveData<HashMap<String, Int>> = answers

    fun logAnswers() {
        val currentAnswers = answers.value ?: HashMap()
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
        questions: List<Int>,
        feedback: String
    ): LiveData<Response<SubmitQuestionnaireResponse>> = liveData {
        val responseLiveData = MutableLiveData<Response<SubmitQuestionnaireResponse>>()
        _isLoading.value = true
        var cFeedback = feedback;
        if(feedback.isEmpty()){
            cFeedback = "Tidak ada umpan balik dari mahasiswa."
        }
        val request = SubmitQuestionnaireRequest(period, classId, questions, cFeedback)
        val client = repository.submitQuestionnaire(request)
        client.enqueue(object : Callback<SubmitQuestionnaireResponse> {
            override fun onResponse(
                call: Call<SubmitQuestionnaireResponse>,
                response: Response<SubmitQuestionnaireResponse>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val errorJson = JSONObject(errorBody)
                        val errorMessage = errorJson.getString("message")
                        _errorMessage.value = errorMessage
                    } catch (e: Exception) {
                        _errorMessage.value = "Terjadi kesalahan pada server"
                    }
                }
                responseLiveData.value = response
            }

            override fun onFailure(call: Call<SubmitQuestionnaireResponse>, t: Throwable) {
                _isLoading.value = false
                val errorBody = (t.message ?: "Unknown error").toResponseBody(null)
                val errorResponse = Response.error<SubmitQuestionnaireResponse>(500, errorBody)
                responseLiveData.value = errorResponse
                _errorMessage.value = t.message ?: "Terjadi kesalahan pada koneksi"
            }
        })
        emitSource(responseLiveData)
    }


    fun fetchScoreResult(classId: String, period: String) {
        _isLoading.value = true
        val client = repository.getQuestionnaire(classId, period)
        client.enqueue(object : Callback<GetQuestionnaireResponse> {
            override fun onResponse(
                call: Call<GetQuestionnaireResponse>,
                response: Response<GetQuestionnaireResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val data = response.body()?.data?.scoreResult
                    val scoreAverage = response.body()?.data?.scoreAverage
                    val reccomendation = response.body()?.data?.dimensionReccomendation
                    val reccomendationText = response.body()?.data?.reccomendation
                    if (data != null) {
                        _scoreResult.postValue(data.mapNotNull { it })

                    } else {
                        _errorMessage.postValue("No score result found")
                    }
                    if (reccomendation != null) {
                        _reccomendation.postValue(reccomendation!!)
                        _reccomendationText.postValue(reccomendationText!!)
                    } else {
                        _errorMessage.postValue("No reccomendation found")
                    }
                    if(scoreAverage != null){
                        _scoreAverage.postValue(scoreAverage.mapNotNull { it })
                    } else {
                        _errorMessage.postValue("No score average found")
                    }
                } else {
                    _errorMessage.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<GetQuestionnaireResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue(t.message)
            }
        })
    }


    fun fetchAvailablePeriod(classId: String) {
        _isLoading.value = true
        val client = repository.getAvailablePeriod(classId)
        client.enqueue(object : Callback<ShowAvailablePeriodResponse> {
            override fun onResponse(
                call: Call<ShowAvailablePeriodResponse>,
                response: Response<ShowAvailablePeriodResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if(data != null) {
                        _periods.postValue(data.mapNotNull { it })
                    }
                } else {
                    _errorMessage.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<ShowAvailablePeriodResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue(t.message)
            }
        })
    }

    fun fetchStudentProgress(classId: String, type:String){
        _isLoading.value = true
        val client =  repository.getStudentProgress(classId, type)
        client.enqueue(object  : Callback<StudentProgressResponse>{

            override fun onResponse(
                call: Call<StudentProgressResponse>,
                response: Response<StudentProgressResponse>
            ) {
                _isLoading.postValue(false)
                if(response.isSuccessful){
                    val data = response.body()?.data
                    if(data != null) {
                        _studentProgress.postValue(data!!)
                    }

                } else{
                    _errorMessage.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<StudentProgressResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue(t.message)
            }
        })
    }






}