package com.dl2lab.srolqs.ui.kegiatan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dl2lab.srolqs.data.remote.request.TambahKegiatanRequest
import com.dl2lab.srolqs.data.remote.request.UpdateKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.remote.response.TambahKegiatanResponse
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import kotlinx.coroutines.launch

class KegiatanViewModel(private val kegiatanRepository: KegiatanRepository) : ViewModel() {

    private val _addKegiatanResult = MutableLiveData<Result<TambahKegiatanResponse>>()
    val addKegiatanResult: LiveData<Result<TambahKegiatanResponse>> = _addKegiatanResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _kegiatanList = MutableLiveData<List<KegiatanItem>>()
    val kegiatanList: LiveData<List<KegiatanItem>> = _kegiatanList

    private val _kegiatanItem = MutableLiveData<KegiatanItem>()
    val kegiatanItem: LiveData<KegiatanItem> = _kegiatanItem

    private val _kegiatanDoneList = MutableLiveData<List<KegiatanItem>>()
    val kegiatanDoneList: LiveData<List<KegiatanItem>> = _kegiatanDoneList

    private val _kegiatanNotDoneList = MutableLiveData<List<KegiatanItem>>()
    val kegiatanNotDoneList: LiveData<List<KegiatanItem>> = _kegiatanNotDoneList

    private val _checklistResult = MutableLiveData<BasicResponse>()
    val checklistResult: LiveData<BasicResponse> = _checklistResult

    private val _updateResult = MutableLiveData<Result<TambahKegiatanResponse>>()
    val updateResult: LiveData<Result<TambahKegiatanResponse>> = _updateResult

    private val _deleteResult = MutableLiveData<BasicResponse>()
    val deleteResult: LiveData<BasicResponse> = _deleteResult

    fun addKegiatan(request: TambahKegiatanRequest, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                kegiatanRepository.tambahKegiatan(request, token) { result ->
                    _addKegiatanResult.postValue(result)
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _addKegiatanResult.postValue(Result.failure(e))
                _isLoading.postValue(false)
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun fetchKegiatanList(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            kegiatanRepository.getListKegiatan(token) { result ->
                result.onSuccess { response ->
                    _kegiatanList.postValue(response.data)
                    _errorMessage.postValue(null)
                }.onFailure { exception ->
                    _errorMessage.postValue(exception.message)
                }
                _isLoading.postValue(false)
            }
        }
    }

    fun checklistKegiatan(id: Int, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                kegiatanRepository.checklistKegiatan(token, id) { result ->
                    result.onSuccess { response ->
                        _checklistResult.postValue(response)
                        _errorMessage.postValue(null)
                    }.onFailure { exception ->
                        _errorMessage.postValue(exception.message)
                    }
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                _isLoading.postValue(false)
            }
        }
    }

    fun deleteKegiatan(id: Int, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                kegiatanRepository.deleteKegiatan(token, id) { result ->
                    result.onSuccess { response ->
                        _deleteResult.postValue(response)
                        _errorMessage.postValue(null)
                    }.onFailure { exception ->
                        _errorMessage.postValue(exception.message)
                    }
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                _isLoading.postValue(false)
            }
        }
    }


    fun fetchDetailKegiatan(id: Int, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            kegiatanRepository.getKegiatanDetail(token, id) { result ->
                result.onSuccess {
                    if (it.data != null) {
                        _kegiatanItem.postValue(it.data[0]!!)
                    }
                    _errorMessage.postValue(null)
                }.onFailure { exception ->
                    _errorMessage.postValue(exception.message)
                }
                _isLoading.postValue(false)
            }
        }
    }

    fun updateKegiatan(request: UpdateKegiatanRequest, id: Int, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            kegiatanRepository.updateKegiatan(token, id, request) { result ->
                result.onSuccess {
                    _updateResult.postValue(Result.success(it))
                }.onFailure { exception ->
                    _errorMessage.postValue(exception.message)
                    _updateResult.postValue(Result.failure(exception))
                }
                _isLoading.postValue(false)
            }
        }
    }
}