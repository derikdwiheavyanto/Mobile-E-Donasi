package com.example.e_donasi.utils

import org.json.JSONObject
import retrofit2.Response

object ErrorHelper {

     fun <T>errorBodyHandlerError(response: Response<T>): String?{
         val errorBody = response.errorBody()?.string()
         val message = JSONObject(errorBody ?: "{}")
             .optString("message", "Terjadi kesalahan")

         return message
    }

}