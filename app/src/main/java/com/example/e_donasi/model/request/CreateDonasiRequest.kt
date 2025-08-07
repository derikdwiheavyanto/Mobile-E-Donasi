package com.example.e_donasi.model.request

import java.io.File


data class CreateDonasiRequest(
    val nominal: Int,
    val deskripsi: String,
    val gambar: File?
    )
