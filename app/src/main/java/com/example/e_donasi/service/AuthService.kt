package com.example.e_donasi.service

import com.example.e_donasi.model.request.LoginRequest
import com.example.e_donasi.model.request.RegisterRequest
import com.example.e_donasi.model.response.LoginResponse
import com.example.e_donasi.model.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.*


interface AuthService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("username") username : String,
        @Field("password") password : String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username : String,
        @Field("password") password : String
    ): Response<LoginResponse>

}
