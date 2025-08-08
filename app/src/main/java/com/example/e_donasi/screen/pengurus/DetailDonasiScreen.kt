package com.example.e_donasi.screen.pengurus

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.e_donasi.model.viewModel.PengurusViewModel
import com.example.e_donasi.utils.PrefrenceManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.e_donasi.components.TopBarComponent
import com.example.e_donasi.utils.formatToIndonesian
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailDonasiScreen(
    donasiId: String,
    navController: NavController,
    viewModel: PengurusViewModel = viewModel()
) {
    val context = LocalContext.current
    val donasiDetail by viewModel.detailDonasi.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val deleteLoading by viewModel.deleteLoading.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }


    val messageSuccess by viewModel.messageSuccesss.collectAsState()

    val coroutinScope = rememberCoroutineScope()


    LaunchedEffect(donasiId) {
        val token = PrefrenceManager.getToken(context) ?: ""
        viewModel.getDetailDonasi(token, donasiId)
    }

    LaunchedEffect(messageSuccess) {
        messageSuccess?.let {
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopBarComponent("Detail Donasi",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                donasiDetail != null -> {
                    val item = donasiDetail!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Gambar utama
                        item.gambar?.let { url ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                AsyncImage(
                                    model = url,
                                    contentDescription = "Gambar Donasi",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Informasi dalam Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Tanggal Donasi",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = formatToIndonesian(item.tanggalDonasi ?: "-"),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Nominal",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = "Rp${item.nominal ?: 0}",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Deskripsi",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = item.deskripsi ?: "-",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        // Tombol Edit dan Hapus
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),

                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    // Navigasi ke EditDonasiScreen, sesuaikan dengan route edit kamu
                                    navController.navigate("edit_donasi/${item.id}")
                                }
                            ) {
                                Text("Edit")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    showDeleteDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Delete", color = Color.White)
                            }
                        }
                    }
                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = { Text("Konfirmasi Hapus") },
                            text = { Text("Apakah kamu yakin ingin menghapus donasi ini?") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showDeleteDialog = false
                                        coroutinScope.launch {
                                            val token = PrefrenceManager.getToken(context) ?: ""
                                            viewModel.deleteDonasi(token, item.id ?: "")
                                        }
                                    }
                                ) {
                                    Text("Ya, Hapus")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDeleteDialog = false }
                                ) {
                                    Text("Batal")
                                }
                            }
                        )
                    }
                }

                else -> {
                    Text(
                        text = "Data donasi tidak ditemukan.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }


            }




            if (deleteLoading) {
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
}



