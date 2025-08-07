package com.example.e_donasi.model.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_donasi.model.request.CreateDonasiRequest
import com.example.e_donasi.model.response.donasi.DataDonation
import com.example.e_donasi.service.PengurusService
import com.example.e_donasi.service.client.ApiClient
import com.example.e_donasi.utils.ErrorHelper
import com.example.e_donasi.utils.PrefrenceManager
import com.example.e_donasi.utils.formatBearerToken
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PengurusViewModel : ViewModel() {

    private val _event = MutableSharedFlow<DonasiEvent>()
    val event = _event.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _messageSuccess = MutableStateFlow<String?>(null)
    val messageSuccesss: StateFlow<String?> = _messageSuccess

    private val _createSuccess = MutableStateFlow<Boolean>(false)
    val createSuccess: StateFlow<Boolean> = _createSuccess

    private val _updateSuccess = MutableStateFlow<Boolean>(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess

    private val _deleteSuccess = MutableStateFlow<Boolean>(false)
    val deleteSuccess: StateFlow<Boolean> = _deleteSuccess

    private val _deleteLoading = MutableStateFlow<Boolean>(false)
    val deleteLoading: StateFlow<Boolean> = _deleteLoading

    private val _detailDonasi = MutableStateFlow<DataDonation?>(null)
    val detailDonasi: StateFlow<DataDonation?> = _detailDonasi


    private val _listOfDonation = MutableStateFlow<List<DataDonation>>(emptyList())
    val listOfDonation: StateFlow<List<DataDonation>> = _listOfDonation

    fun getListDonasi(token: String?) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val bearerToken = formatBearerToken(token)
                val response = ApiClient.pengurusService.getAllDonation(bearerToken)

                _isLoading.value = false

                if (response.isSuccessful) {
                    val body = response.body()
                    _listOfDonation.value = body?.data ?: emptyList()
                } else {
                    if (response.code() == 401) {
                        _event.emit(DonasiEvent.ShowToast("Sesi Anda telah habis, silakan login kembali"))
                        _event.emit(DonasiEvent.NavigateToLogin)
                    }
                    val errorMessage = ErrorHelper.errorBodyHandlerError(response)
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
            }
        }
    }

    fun createDonasi(token: String?, createDonasiRequest: CreateDonasiRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val bearerToken = formatBearerToken(token)

                // Convert nominal dan deskripsi ke RequestBody
                val nominalBody = createDonasiRequest.nominal.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())

                val deskripsiBody = createDonasiRequest.deskripsi
                    .toRequestBody("text/plain".toMediaTypeOrNull())

                // Convert gambar ke MultipartBody.Part jika ada
                val imagePart = createDonasiRequest.gambar?.let { file ->
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("gambar", file.name, requestFile)
                }

                // Panggil API
                val response = ApiClient.pengurusService.createDonation(
                    token = bearerToken,
                    nominal = nominalBody,
                    deskripsi = deskripsiBody,
                    gambar = imagePart
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    _isLoading.value = false
                    _createSuccess.value = true
                    _messageSuccess.value = body?.message
                } else {
                    if (response.code() == 401) {
                        _event.emit(DonasiEvent.ShowToast("Sesi Anda telah habis, silakan login kembali"))
                        _event.emit(DonasiEvent.NavigateToLogin)
                    }
                    val errorMessage = ErrorHelper.errorBodyHandlerError(response)
                    _errorMessage.value = errorMessage
                }

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi kesalahan"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getDetailDonasi(token: String, id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val bearerToken = formatBearerToken(token) ?: ""
                val response = ApiClient.pengurusService.getDonasiById(bearerToken, id)
                if (response.isSuccessful) {
                    val body = response.body()
                    _detailDonasi.value = body?.data
                    _isLoading.value = false

                } else {
                    if (response.code() == 401) {
                        _event.emit(DonasiEvent.ShowToast("Sesi Anda telah habis, silakan login kembali"))
                        _event.emit(DonasiEvent.NavigateToLogin)
                    }
                    val errorMessage = ErrorHelper.errorBodyHandlerError(response)
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat detail: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateDonasi(token: String, id: String, donasiRequest: CreateDonasiRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val bearerToken = formatBearerToken(token) ?: ""

                val nominalBody = donasiRequest.nominal.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())

                val deskripsiBody = donasiRequest.deskripsi
                    .toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart = donasiRequest.gambar?.let { file ->
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("gambar", file.name, requestFile)
                }

                val response = ApiClient.pengurusService.updateDonasi(
                    token = bearerToken,
                    id = id,
                    nominal = nominalBody,
                    deskripsi = deskripsiBody,
                    gambar = imagePart
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    _isLoading.value = false
                    _updateSuccess.value = true
                    _messageSuccess.value = body?.message
                } else {
                    if (response.code() == 401) {
                        _event.emit(DonasiEvent.ShowToast("Sesi Anda telah habis, silakan login kembali"))
                        _event.emit(DonasiEvent.NavigateToLogin)
                    }
                    val errorMessage = ErrorHelper.errorBodyHandlerError(response)
                    _errorMessage.value = errorMessage
                }

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi kesalahan"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteDonasi(token: String?, id: String) {
        _deleteLoading.value = true
        viewModelScope.launch {
            try {
                val bearerToken = formatBearerToken(token) ?: ""
                val response = ApiClient.pengurusService.deleteDonasi(bearerToken, id)

                if (response.isSuccessful) {
                    val body = response.body()
                    _deleteLoading.value = false
                    _messageSuccess.value = body?.message
                } else {
                    if (response.code() == 401) {
                        _event.emit(DonasiEvent.ShowToast("Sesi Anda telah habis, silakan login kembali"))
                        _event.emit(DonasiEvent.NavigateToLogin)
                    }
                    val errorMessage = ErrorHelper.errorBodyHandlerError(response)
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi kesalahan"
            } finally {
                _deleteLoading.value = false
            }
        }
    }

    fun resetState() {
        _isLoading.value = false
        _errorMessage.value = null
    }

    sealed class DonasiEvent {
        object NavigateToLogin : DonasiEvent()
        data class ShowToast(val message: String) : DonasiEvent()
    }

}