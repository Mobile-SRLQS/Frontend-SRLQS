package com.dl2lab.srolqs.data.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateKegiatanRequest(

	@field:SerializedName("nama_kegiatan")
	val namaKegiatan: String? = null,

	@field:SerializedName("tipe_kegiatan")
	val tipeKegiatan: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("tenggat")
	val tenggat: String? = null
)
