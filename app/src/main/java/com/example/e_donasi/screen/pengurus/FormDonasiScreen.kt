package com.example.e_donasi.screen.pengurus

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.e_donasi.components.TopBarComponent
import com.github.dhaval2404.imagepicker.ImagePicker
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_donasi.model.request.CreateDonasiRequest
import com.example.e_donasi.model.viewModel.PengurusViewModel
import com.example.e_donasi.utils.PrefrenceManager
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDonasiScreen(
    navController: NavController,
    donasiId: String? = null,
    pengurusViewModel: PengurusViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }


    // State form
    var nominal by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var oldImage by remember { mutableStateOf<String?>(null) }

    val isLoading by pengurusViewModel.isLoading.collectAsState()
    val messageSuccess by pengurusViewModel.messageSuccesss.collectAsState()
    val errorMessage by pengurusViewModel.errorMessage.collectAsState()

    val createDonasiSuccess by pengurusViewModel.createSuccess.collectAsState()

    val updateDonasiSucces by pengurusViewModel.updateSuccess.collectAsState()

    val deleteDonasiSuccess by pengurusViewModel.deleteSuccess.collectAsState()

    val detailDonasi by pengurusViewModel.detailDonasi.collectAsState()



    LaunchedEffect(donasiId) {
        if (!donasiId.isNullOrEmpty()) {
            val token = PrefrenceManager.getToken(context) ?: ""
            pengurusViewModel.getDetailDonasi(token, donasiId)
        }
    }

    LaunchedEffect(detailDonasi) {
        detailDonasi?.let { donasi ->
            nominal = donasi.nominal?.toString() ?: ""
            deskripsi = donasi.deskripsi ?: ""
            oldImage = donasi.gambar

        }
    }

    LaunchedEffect(createDonasiSuccess, updateDonasiSucces, deleteDonasiSuccess, errorMessage) {
        if (createDonasiSuccess) {
            Toast.makeText(context, messageSuccess ?: "Sukses", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }

        if (updateDonasiSucces) {
            Toast.makeText(context, messageSuccess ?: "Sukses", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }

        if (deleteDonasiSuccess) {
            Toast.makeText(context, messageSuccess ?: "Sukses", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }

        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            pengurusViewModel.resetDetailState()
            pengurusViewModel.resetCreateState()
            pengurusViewModel.resetUpdateState()
            pengurusViewModel.resetDeleteState()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
        } else if (result.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(result.data), Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopBarComponent(
                title = if (donasiId == null) "Form Donasi" else "Edit Donasi",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = nominal,
                    onValueChange = { nominal = it },
                    label = { Text("Nominal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        ImagePicker.with(context as Activity)
                            .crop()
                            .compress(1024)
                            .galleryOnly()
                            .createIntent { intent -> launcher.launch(intent) }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pilih Gambar")
                }

                selectedImageUri?.let { uri ->
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = uri,
                        contentDescription = "Preview Gambar",
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )
                }

                oldImage?.let { uri ->
                    if (selectedImageUri == null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = uri,
                            contentDescription = "Preview Gambar",
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nominal.isBlank() || deskripsi.isBlank()) {
                            Toast.makeText(context, "Mohon isi semua data!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        showDialog = true // Tampilkan konfirmasi
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (donasiId == null) "Kirim Donasi" else "Update Donasi")
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(text = if (donasiId == null) "Konfirmasi Kirim" else "Konfirmasi Update")
                },
                text = {
                    Text(text = "Apakah kamu yakin ingin ${if (donasiId == null) "mengirim" else "mengupdate"} donasi ini?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false

                            coroutineScope.launch {
                                val file = selectedImageUri?.let { uri ->
                                    val inputStream: InputStream? =
                                        context.contentResolver.openInputStream(uri)
                                    val tempFile =
                                        File.createTempFile("upload", ".jpg", context.cacheDir)
                                    inputStream?.use { input ->
                                        FileOutputStream(tempFile).use { output ->
                                            input.copyTo(output)
                                        }
                                    }
                                    tempFile
                                }

                                val token = PrefrenceManager.getToken(context)

                                if (donasiId == null) {
                                    if (file == null) {
                                        Toast.makeText(context, "Pilih gambar!", Toast.LENGTH_SHORT)
                                            .show()
                                        return@launch
                                    }

                                    val request = CreateDonasiRequest(
                                        nominal = nominal.toInt(),
                                        deskripsi = deskripsi,
                                        gambar = file
                                    )
                                    pengurusViewModel.createDonasi(token, request)
                                } else {
                                    val request = CreateDonasiRequest(
                                        nominal = nominal.toInt(),
                                        deskripsi = deskripsi,
                                        gambar = file
                                    )
                                    pengurusViewModel.updateDonasi(
                                        token = token ?: "",
                                        id = donasiId,
                                        donasiRequest = request
                                    )
                                }
                            }
                        }
                    ) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }

        if (isLoading) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {},
                title = { Text("Loading") },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Memproses data...")
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FormDonasiScreenPreview() {
    FormDonasiScreen(navController = rememberNavController())
}
