package com.example.e_donasi.model.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_donasi.model.request.LoginRequest
import com.example.e_donasi.model.request.RegisterRequest
import com.example.e_donasi.navigation.Screen
import com.example.e_donasi.service.client.ApiClient
import com.example.e_donasi.utils.ErrorHelper
import com.example.e_donasi.utils.PrefrenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _registerSuccess = MutableStateFlow<Boolean>(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun login(loginRequest: LoginRequest, context: Context) {
        _isLoading.value = true
        viewModelScope.launch {
            try {

                val response = ApiClient.authService.login(
                    username = loginRequest.username,
                    password = loginRequest.password
                )
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    val token = body?.data?.token ?: ""
                    val id = body?.data?.id ?: ""
                    val role = body?.data?.role?.name ?: ""
                    val name = body?.data?.name ?: ""

                    PrefrenceManager.setToken(context, token)
                    PrefrenceManager.saveUserId(context, id)
                    PrefrenceManager.saveUserFullname(context, name)
                    PrefrenceManager.saveUserRole(context, role)

                    Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()

                    _loginSuccess.value = true
                } else {
                    val errorMessage = ErrorHelper.errorBodyHandlerError(response)
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
            }
        }
    }

    fun register(registerRequest: RegisterRequest, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.authService.register(
                    name = registerRequest.name,
                    username = registerRequest.username,
                    password = registerRequest.password
                )

                _isLoading.value = false

                if (response.isSuccessful) {
                    Toast.makeText(context,"Register Berhasil", Toast.LENGTH_SHORT).show()
                    _registerSuccess.value = true
                } else {
                    val errorMessage = ErrorHelper.errorBodyHandlerError(response)
                    _errorMessage.value = errorMessage
                }

            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Terjadi Kesalahan ${e.localizedMessage}"
            }
        }
    }

    fun resetState() {
        _loginSuccess.value = false
        _registerSuccess.value = false
        _errorMessage.value = null
    }


}