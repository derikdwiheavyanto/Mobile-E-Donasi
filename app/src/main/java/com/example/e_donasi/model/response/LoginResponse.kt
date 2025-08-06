package com.example.e_donasi.model.response


data class LoginResponse(
    val status: Int,
    val message: String,
    val data: LoginData?,
)


data class LoginData(
    val id: String,
    val name: String,
    val username: String,
    val role: Role,
    val token: String

)

data class Role(
    val id: Int,
    val name: String
)
