package com.dl2lab.srolqs.data.repository

import com.dl2lab.srolqs.data.remote.request.TambahKegiatanRequest
import com.dl2lab.srolqs.data.remote.request.UpdateKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.GetKegiatanDetailResponse
import com.dl2lab.srolqs.data.remote.response.GetKegiatanResponse
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.remote.response.TambahKegiatanResponse
import com.dl2lab.srolqs.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KegiatanRepository {

    fun tambahKegiatan(request: TambahKegiatanRequest, token: String, onResult: (Result<TambahKegiatanResponse>) -> Unit) {
        val apiServiceSecured = ApiConfig.getApiServiceSecured(token)
        apiServiceSecured.tambahKegiatan(request).enqueue(object : Callback<TambahKegiatanResponse> {
            override fun onResponse(
                call: Call<TambahKegiatanResponse>,
                response: Response<TambahKegiatanResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(Result.success(it))
                    } ?: onResult(Result.failure(Throwable("Response body is null")))
                } else {
                    onResult(Result.failure(Throwable("Failed to add kegiatan: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<TambahKegiatanResponse>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }
    fun getListKegiatan(token: String, onResult: (Result<GetKegiatanResponse>) -> Unit) {
        val apiServiceSecured = ApiConfig.getApiServiceSecured(token)
        apiServiceSecured.getListKegiatan().enqueue(object : Callback<GetKegiatanResponse> {
            override fun onResponse(
                call: Call<GetKegiatanResponse>,
                response: Response<GetKegiatanResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(Result.success(it))
                    } ?: onResult(Result.failure(Throwable("Response body is null")))
                } else {
                    onResult(Result.failure(Throwable("Gagal mendapatkan list kegiatan: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<GetKegiatanResponse>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    fun getListKegiatanByType(token: String, type:String, onResult: (Result<GetKegiatanResponse>) -> Unit) {
        val apiServiceSecured = ApiConfig.getApiServiceSecured(token)
        apiServiceSecured.getKegiatanByType(type).enqueue(object : Callback<GetKegiatanResponse> {
            override fun onResponse(
                call: Call<GetKegiatanResponse>,
                response: Response<GetKegiatanResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(Result.success(it))
                    } ?: onResult(Result.failure(Throwable("Response body is null")))
                } else {
                    onResult(Result.failure(Throwable("Gagal mendapatkan list kegiatan: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<GetKegiatanResponse>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    fun deleteKegiatan(token: String, id:Int, onResult: (Result<BasicResponse>) -> Unit) {
        val apiServiceSecured = ApiConfig.getApiServiceSecured(token)
        apiServiceSecured.deleteKegaiatan(id).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(Result.success(it))
                    } ?: onResult(Result.failure(Throwable("Response body is null")))
                } else {
                    onResult(Result.failure(Throwable("Gagal mendapatkan list kegiatan: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    fun checklistKegiatan(token: String, id:Int, onResult: (Result<BasicResponse>) -> Unit) {
        val apiServiceSecured = ApiConfig.getApiServiceSecured(token)
        apiServiceSecured.checklistKegiatan(id).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(Result.success(it))
                    } ?: onResult(Result.failure(Throwable("Response body is null")))
                } else {
                    onResult(Result.failure(Throwable("Gagal mendapatkan list kegiatan: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    fun getKegiatanDetail(token: String, id:Int, onResult: (Result<GetKegiatanDetailResponse>) -> Unit) {
        val apiServiceSecured = ApiConfig.getApiServiceSecured(token)
        apiServiceSecured.getKegiatanDetail(id).enqueue(object : Callback<GetKegiatanDetailResponse> {
            override fun onResponse(
                call: Call<GetKegiatanDetailResponse>,
                response: Response<GetKegiatanDetailResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(Result.success(it))
                    } ?: onResult(Result.failure(Throwable("Response body is null")))
                } else {
                    onResult(Result.failure(Throwable("Gagal mendapatkan list kegiatan: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<GetKegiatanDetailResponse>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }

    fun updateKegiatan(token: String, id:Int, objectDTO:UpdateKegiatanRequest, onResult: (Result<TambahKegiatanResponse>) -> Unit) {
        val apiServiceSecured = ApiConfig.getApiServiceSecured(token)
        apiServiceSecured.updateKegiatan(id, objectDTO).enqueue(object : Callback<TambahKegiatanResponse> {
            override fun onResponse(
                call: Call<TambahKegiatanResponse>,
                response: Response<TambahKegiatanResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(Result.success(it))
                    } ?: onResult(Result.failure(Throwable("Pastikan data yang diubah tidak kosong")))
                } else {
                    onResult(Result.failure(Throwable("Gagal mengubah kegiatan: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<TambahKegiatanResponse>, t: Throwable) {
                onResult(Result.failure(t))
            }
        })
    }


}
