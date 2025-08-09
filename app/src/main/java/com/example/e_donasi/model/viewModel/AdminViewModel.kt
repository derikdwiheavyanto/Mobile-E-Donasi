package com.example.e_donasi.model.viewModel

import android.util.Log
import com.example.e_donasi.model.response.UserPengurus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_donasi.service.client.ApiClient
import com.example.e_donasi.utils.ErrorHelper
import com.example.e_donasi.utils.formatBearerToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val _event = MutableSharedFlow<AdminEvent>()
    val event = _event.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _messageSuccess = MutableStateFlow<String?>(null)
    val messageSuccess: StateFlow<String?> = _messageSuccess

    private val _listUserPengurus = MutableStateFlow<List<UserPengurus>>(emptyList())
    val listUserPengurus: StateFlow<List<UserPengurus>> = _listUserPengurus


    fun getAllUserPengurus(token: String?) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.adminService.getAllUserPengurus(formatBearerToken(token))
                if (response.isSuccessful) {
                    _listUserPengurus.value = response.body()?.data ?: emptyList()
                } else {
                    handleError(response.code(), response)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun deleteUserPengurus(token: String?, id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.adminService.deleteUserPengurus(formatBearerToken(token), id)
                if (response.isSuccessful) {
                    _messageSuccess.value = response.body()?.message
                    getAllUserPengurus(token)
                } else {
                    handleError(response.code(), response)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun changeStatusActiveUser(token: String?, id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.adminService.changeStatusActiveUser(formatBearerToken(token), id)
                if (response.isSuccessful) {
                    _messageSuccess.value = response.body()?.message
                    getAllUserPengurus(token)
                } else {
                    handleError(response.code(), response)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun <T> handleError(code: Int, response: retrofit2.Response<T>) {
        if (code == 401) {
            _event.emit(AdminEvent.ShowToast("Sesi Anda telah habis, silakan login kembali"))
            _event.emit(AdminEvent.NavigateToLogin)
        }else{
            _errorMessage.value = ErrorHelper.errorBodyHandlerError(response)
        }
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    sealed class AdminEvent {
        object NavigateToLogin : AdminEvent()
        data class ShowToast(val message: String) : AdminEvent()
    }
}
