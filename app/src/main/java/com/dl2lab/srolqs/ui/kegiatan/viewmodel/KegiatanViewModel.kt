package com.dl2lab.srolqs.ui.kegiatan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dl2lab.srolqs.data.remote.request.TambahKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.DataItem
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
            }
        }
    }

    fun fetchKegiatanList(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            kegiatanRepository.getListKegiatan(token) { result ->
                result.onSuccess {
                    _kegiatanList.postValue(it.data)
                    _errorMessage.postValue(null)
                }.onFailure { exception ->
                    _errorMessage.postValue(exception.message)
                }
                _isLoading.postValue(false)
            }
        }
    }

}
