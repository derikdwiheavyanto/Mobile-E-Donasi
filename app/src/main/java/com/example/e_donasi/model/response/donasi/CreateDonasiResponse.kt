package com.example.e_donasi.model.response.donasi

import com.google.gson.annotations.SerializedName

data class CreateDonasiResponse (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : Donasi?   = Donasi()

)
data class Donasi (

    @SerializedName("id"             ) var id            : String? = null,
    @SerializedName("order_id"       ) var orderId       : String? = null,
    @SerializedName("tanggal_donasi" ) var tanggalDonasi : String? = null,
    @SerializedName("nominal"        ) var nominal       : Int?    = null,
    @SerializedName("deskripsi"      ) var deskripsi     : String? = null,
    @SerializedName("gambar"         ) var gambar        : String? = null,
    @SerializedName("created_at"     ) var createdAt     : String? = null,
    @SerializedName("updated_at"     ) var updatedAt     : String? = null,
    @SerializedName("id_user"        ) var idUser        : String? = null

)