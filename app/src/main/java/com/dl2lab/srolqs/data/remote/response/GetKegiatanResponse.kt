package com.dl2lab.srolqs.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetKegiatanResponse(

	@field:SerializedName("data")
	val data: List<KegiatanItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class KegiatanItem(

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
) : Parcelable
