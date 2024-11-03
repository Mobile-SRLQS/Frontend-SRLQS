package com.dl2lab.srolqs.data.remote.response

import com.google.gson.annotations.SerializedName

data class TambahKegiatanResponse(

	@field:SerializedName("data")
	val data: KegiatanData,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class KegiatanData(

	@field:SerializedName("is_done")
	val isDone: Boolean,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("nama_kegiatan")
	val namaKegiatan: String,

	@field:SerializedName("tipe_kegiatan")
	val tipeKegiatan: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("catatan")
	val catatan: String,

	@field:SerializedName("tenggat")
	val tenggat: String,

	@field:SerializedName("id")
	val id: Int
)
