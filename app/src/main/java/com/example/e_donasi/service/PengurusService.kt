package com.example.e_donasi.service

import com.example.e_donasi.model.response.donasi.CreateDonasiResponse
import com.example.e_donasi.model.response.donasi.DataDonation
import com.example.e_donasi.model.response.donasi.DetailDonasiResponse
import com.example.e_donasi.model.response.donasi.DonasiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PengurusService {

    @GET("donasi")
    suspend fun getAllDonation(
        @Header("Authorization") token: String?
    ): Response<DonasiResponse>

    @Multipart
    @POST("donasi")
    suspend fun createDonation(
        @Header("Authorization") token: String?,
        @Part("nominal") nominal: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part?
    ): Response<CreateDonasiResponse>

    @GET("donasi/{id}")
    suspend fun getDonasiById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<DetailDonasiResponse>

    @Multipart
    @PATCH("donasi/{id}")
    suspend fun updateDonasi(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part("nominal") nominal: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part?
    ): Response<CreateDonasiResponse>

    @DELETE("donasi/{id}")
    suspend fun deleteDonasi(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<CreateDonasiResponse>
}