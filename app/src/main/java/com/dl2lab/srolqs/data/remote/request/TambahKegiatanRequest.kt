package com.dl2lab.srolqs.data.remote.request

import com.google.gson.annotations.SerializedName

class TambahKegiatanRequest (
    @SerializedName("nama_kegiatan") val namaKegiatan: String,
    @SerializedName("tipe_kegiatan") val tipeKegiatan: String,
    @SerializedName("tenggat") val tenggat: String,
    @SerializedName("catatan") val catatan: String,
    @SerializedName("link") val link: String
    )