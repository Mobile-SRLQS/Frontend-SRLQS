package com.dl2lab.srolqs.data.repository

import com.dl2lab.srolqs.data.remote.request.TambahKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.GetKegiatanResponse
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
}
