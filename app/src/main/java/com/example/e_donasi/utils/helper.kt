package com.example.e_donasi.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatBearerToken(token: String?): String? {
    return token?.let { "Bearer $it" }
}

fun formatToIndonesian(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(dateString)

        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        "${outputFormat.format(date)} WIB"
    } catch (e: Exception) {
        dateString
    }
}