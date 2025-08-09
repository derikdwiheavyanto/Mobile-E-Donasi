package com.example.e_donasi.screen.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.e_donasi.components.TopBarComponent
import com.example.e_donasi.model.viewModel.AdminViewModel
import com.example.e_donasi.model.viewModel.PengurusViewModel
import com.example.e_donasi.navigation.Screen
import com.example.e_donasi.utils.PrefrenceManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserPengurusScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel()
) {
    val context = LocalContext.current
    val pengurusList by viewModel.listUserPengurus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    var showDeleteDialog by remember { mutableStateOf<String?>(null) }
    var showStatusDialog by remember { mutableStateOf<String?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }


    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        val token = PrefrenceManager.getToken(context)
        viewModel.getAllUserPengurus(token)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is AdminViewModel.AdminEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is AdminViewModel.AdminEvent.NavigateToLogin -> {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarComponent(
                title = "Kelola Pengurus",

                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Tambah Pengurus")
                    }

                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                pengurusList.isNotEmpty() -> {
                    Column(Modifier.padding(16.dp)) {
                        pengurusList.forEach { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        "Nama: ${user.name ?: "-"}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        "Username: ${user.username ?: "-"}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Row {
                                        Button(
                                            onClick = { showStatusDialog = user.id ?: "" },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(if (user.active == true) "Nonaktifkan" else "Aktifkan")
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Button(
                                            onClick = { showDeleteDialog = user.id ?: "" },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                        ) {
                                            Text("Hapus", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        "Tidak ada data pengurus.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            showDeleteDialog?.let { id ->
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = null },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Apakah yakin ingin menghapus pengurus ini?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    val token = PrefrenceManager.getToken(context) ?: ""
                                    viewModel.deleteUserPengurus(token, id)
                                    showDeleteDialog = null
                                }
                            }
                        ) { Text("Ya, Hapus") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = null }) {
                            Text("Batal")
                        }
                    }
                )
            }

            showStatusDialog?.let { id ->
                AlertDialog(
                    onDismissRequest = { showStatusDialog = null },
                    title = { Text("Konfirmasi Ubah Status") },
                    text = { Text("Ubah status aktif user ini?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    val token = PrefrenceManager.getToken(context) ?: ""
                                    viewModel.changeStatusActiveUser(token, id)
                                    showStatusDialog = null
                                }
                            }
                        ) { Text("Ya, Ubah") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStatusDialog = null }) {
                            Text("Batal")
                        }
                    }
                )
            }

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Konfirmasi Logout") },
                    text = { Text("Apakah Anda yakin ingin logout?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    PrefrenceManager.clearAllPreference(context)
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                                showLogoutDialog = false
                            }
                        ) {
                            Text("Ya, Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
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
                            Text("Sedang memuat...")
                        }
                    }
                )
            }

            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = { Text("Fitur Belum Tersedia") },
                    text = { Text("Maaf, fitur ini sedang dalam pengembangan dan belum dapat digunakan.") },
                    confirmButton = {
                        TextButton(onClick = { showAddDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }

            if (!errorMessage.isNullOrEmpty()) {
                AlertDialog(
                    onDismissRequest = { viewModel.clearErrorMessage() },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearErrorMessage() }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Terjadi Kesalahan") },
                    text = { Text(errorMessage ?: "") }
                )
            }

        }
    }
}

