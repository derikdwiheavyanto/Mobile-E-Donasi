package com.example.e_donasi.model.response


data class RegisterResponse(
    val status: Int,
    val message: String,
    val data: RegisterUser?
)


data class RegisterUser(
    val id: String,
    val name: String,
    val username: String
)
