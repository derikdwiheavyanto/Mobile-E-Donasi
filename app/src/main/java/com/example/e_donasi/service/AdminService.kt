package com.example.e_donasi.service

import com.example.e_donasi.model.response.ChangeStatusResponse
import com.example.e_donasi.model.response.PengurusResponse
import com.example.e_donasi.model.response.donasi.DonasiResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AdminService {
    @GET("manajemen-user")
    suspend fun getAllUserPengurus(
        @Header("Authorization") token: String?
    ): Response<PengurusResponse>

    @PATCH("manajemen-user/{id}")
    suspend fun changeStatusActiveUser(
        @Header("Authorization") token: String?,
        @Path("id") id: String,
    ): Response<ChangeStatusResponse>

    @DELETE("manajemen-user/{id}")
    suspend fun deleteUserPengurus(
        @Header("Authorization") token: String?,
        @Path("id") id: String,
    ): Response<ChangeStatusResponse>

}