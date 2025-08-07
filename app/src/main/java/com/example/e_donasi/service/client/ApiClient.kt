package com.example.e_donasi.service.client

import com.example.e_donasi.service.AdminService
import com.example.e_donasi.service.AuthService
import com.example.e_donasi.service.PengurusService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://apie-donasi.vercel.app/api/"

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val adminService: AdminService by lazy {
        retrofit.create(AdminService::class.java)
    }

    val pengurusService: PengurusService by lazy {
        retrofit.create(PengurusService::class.java)
    }


}