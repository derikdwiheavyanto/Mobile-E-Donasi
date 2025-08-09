package com.example.e_donasi.model.response

import com.google.gson.annotations.SerializedName

data class ChangeStatusResponse (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : Data?   = Data()

)

data class Data (

    @SerializedName("id"         ) var id        : String?  = null,
    @SerializedName("name"       ) var name      : String?  = null,
    @SerializedName("username"   ) var username  : String?  = null,
    @SerializedName("active"     ) var active    : Boolean? = null,
    @SerializedName("created_at" ) var createdAt : String?  = null,
    @SerializedName("updated_at" ) var updatedAt : String?  = null

)
