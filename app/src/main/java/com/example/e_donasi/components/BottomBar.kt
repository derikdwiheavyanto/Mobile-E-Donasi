package com.example.e_donasi.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_donasi.navigation.Screen

/**
 * Data class `BottomNavItem` merepresentasikan satu item pada navigasi bawah (BottomBar).
 *
 * @param route rute navigasi dari halaman yang akan dituju.
 * @param icon composable yang merepresentasikan ikon tampilan dari item ini.
 */
data class BottomNavItem(
    val route: String,
    val icon: @Composable () -> Unit
)

/**
 * Composable `BottomBar` menampilkan bilah navigasi bawah dengan dua item utama:
 * Home dan Profile. Menyesuaikan tampilan berdasarkan halaman aktif.
 *
 * @param navController controller navigasi untuk mengatur navigasi antar halaman.
 * @param modifier modifier opsional untuk penyesuaian tampilan luar komponen.
 */
@Composable
fun BottomBar(navController: NavHostController, modifier: Modifier = Modifier) {
    // Mendapatkan route halaman yang sedang aktif
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Daftar item menu yang tersedia di navigasi bawah
    val itemsMenu = listOf(
        BottomNavItem(Screen.Home.route) {
            Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
        }
    )

    // Tampilan dasar background BottomBar
    Surface(
        tonalElevation = 4.dp,
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        // Susunan horizontal item menu
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsMenu.forEach { item ->
                BottomBarItem(item, currentRoute, navController)
            }
        }
    }
}

/**
 * Composable `BottomBarItem` merepresentasikan satu item di dalam BottomBar.
 * Menampilkan ikon dan label serta menangani logika navigasi dan animasi.
 *
 * @param item objek item navigasi bawah.
 * @param currentRoute route halaman yang sedang aktif saat ini.
 * @param navController controller navigasi untuk berpindah halaman.
 */
@Composable
fun BottomBarItem(
    item: BottomNavItem,
    currentRoute: String?,
    navController: NavHostController
) {
    val isSelected = currentRoute == item.route

    // Animasi perubahan ukuran ikon dan teks ketika dipilih
    val floatAnimSpec = tween<Float>(700, easing = FastOutSlowInEasing)

    val iconSize by animateFloatAsState(
        targetValue = if (isSelected) 28f else 24f,
        animationSpec = floatAnimSpec,
        label = "iconSize"
    )

    val textSize by animateFloatAsState(
        targetValue = if (isSelected) 12f else 10f,
        animationSpec = floatAnimSpec,
        label = "textSize"
    )

    val iconColor = if (isSelected) Color(0xFF27548A) else Color.Gray

    // Tampilan satu item navigasi
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(72.dp)
            .clickable {
                if (!isSelected) {
                    navController.navigate(item.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
    ) {
        CompositionLocalProvider(LocalContentColor provides iconColor) {
            Box(modifier = Modifier.size(iconSize.dp)) {
                item.icon()
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.route.replaceFirstChar { it.uppercase() },
            color = iconColor,
            fontSize = textSize.sp,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

/**
 * Preview `BottomBar` yang ditampilkan di Android Studio Preview.
 * Menggunakan NavController dummy untuk pratinjau tampilan.
 */
@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    BottomBar(navController = rememberNavController())
}
