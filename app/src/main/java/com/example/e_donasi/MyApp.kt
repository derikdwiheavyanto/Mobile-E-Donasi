package com.example.e_donasi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.e_donasi.components.BottomBar
import com.example.e_donasi.navigation.Screen
import com.example.e_donasi.navigation.SetupNavGraph

/**
 * Fungsi utama untuk menjalankan struktur UI aplikasi.
 * Menampilkan navigasi dan bottom bar sesuai dengan halaman yang sedang aktif.
 *
 * @param navController controller navigasi yang mengelola perpindahan antar layar.
 */
@Composable
fun MyApp(navController: NavHostController) {
    // Mengambil entry (halaman) aktif saat ini dari backstack
    val currentBackStack by navController.currentBackStackEntryAsState()

    // Mengambil route (nama halaman) dari entry aktif
    val currentRoute = currentBackStack?.destination?.route

    // Menentukan apakah bottom bar perlu ditampilkan berdasarkan halaman yang sedang aktif
    val showBottomBar =  listOf(Screen.Home.route, Screen.Profile.route ).contains(currentRoute)

    // Box sebagai wadah utama layout
    Box(modifier = Modifier.fillMaxSize()) {

        // Scaffold menyusun struktur layout utama aplikasi (topBar, bottomBar, dan konten)
        Scaffold(
            containerColor = Color.Transparent, // Latar belakang transparan

            // Bottom bar hanya muncul di halaman Home dan Profile
            bottomBar = {
                if (showBottomBar) {
                    BottomBar(
                        navController = navController,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars) // Beri padding agar tidak bentrok dengan sistem navigasi Android
                    )
                }
            }
        ) { contentPadding ->
            // Menampilkan isi halaman (konten utama) berdasarkan konfigurasi navigasi
            SetupNavGraph(
                navController = navController,
                modifier = Modifier.padding(contentPadding) // Menyesuaikan padding dari Scaffold
            )
        }
    }
}
