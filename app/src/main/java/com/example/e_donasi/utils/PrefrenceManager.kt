package com.example.e_donasi.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object PrefrenceManager {

    private val Context.dataStore by preferencesDataStore("user_prefs")

    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    private val USER_FULLNAME_KEY = stringPreferencesKey("fullname")

    private val USER_ID_KEY = stringPreferencesKey("user_id")

    private val USER_ROLE = stringPreferencesKey("role")

    suspend fun setToken(context: Context, token: String){
        context.dataStore.edit{
            prefs -> prefs[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(context: Context): String?{
        val prefs = context.dataStore.data.first()
        return prefs[TOKEN_KEY]
    }

    suspend fun saveUserId(context: Context, id: String) {
        context.dataStore.edit { prefs -> prefs[USER_ID_KEY] = id }
    }

    suspend fun saveUserRole(context: Context, role: String){
        context.dataStore.edit { prefs -> prefs[USER_ROLE] = role }
    }

    suspend fun getUserRole(context: Context): String?{
        val prefs = context.dataStore.data.first()
        return prefs[USER_ROLE]
    }

    suspend fun getUserId(context: Context): String? {
        val prefs = context.dataStore.data.first()
        return prefs[USER_ID_KEY]
    }

    suspend fun saveUserFullname(context: Context, fullname: String) {
        context.dataStore.edit { prefs -> prefs[USER_FULLNAME_KEY] = fullname }
    }



    suspend fun getUserFullname(context: Context): String? {
        val prefs = context.dataStore.data.first()
        return prefs[USER_FULLNAME_KEY]
    }

    suspend fun clearAllPreference(context: Context){
        context.dataStore.edit { it.clear() }
    }
}