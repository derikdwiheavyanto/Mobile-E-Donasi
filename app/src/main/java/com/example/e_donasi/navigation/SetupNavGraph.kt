package com.example.e_donasi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.praktikum_kel1_2.screen.*
import com.example.e_donasi.screen.LoginScreen
import com.example.e_donasi.screen.RegisterScreen
import com.example.e_donasi.screen.SplashScreen

/**
 * Fungsi `SetupNavGraph` digunakan untuk mengatur struktur navigasi di aplikasi.
 * Di dalamnya didefinisikan semua halaman yang dapat dinavigasi beserta argumen yang dibutuhkan (jika ada).
 *
 * @param navController controller yang digunakan untuk melakukan navigasi antar halaman.
 * @param modifier modifier opsional untuk styling tambahan pada `NavHost`.
 */
@Composable
fun SetupNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route, // Halaman pertama saat aplikasi dijalankan
        modifier = modifier
    ) {
        // Rute ke halaman Home
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }


        // Rute ke halaman Login
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }

        // Rute ke halaman Register
        composable(route = Screen.Register.route) {
            RegisterScreen(navController)
        }



        composable (route = Screen.SplashScreen.route){
            SplashScreen(navController)
        }
    }
}
