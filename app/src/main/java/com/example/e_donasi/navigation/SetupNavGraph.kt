package com.example.e_donasi.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.e_donasi.screen.HomeScreen
import com.example.e_donasi.screen.LoginScreen
import com.example.e_donasi.screen.RegisterScreen
import com.example.e_donasi.screen.SplashScreen
import com.example.e_donasi.screen.admin.AdminUserPengurusScreen
import com.example.e_donasi.screen.pengurus.DetailDonasiScreen
import com.example.e_donasi.screen.pengurus.FormDonasiScreen
import com.example.e_donasi.screen.pengurus.ProfileScreen

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
        startDestination = Screen.SplashScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(route = Screen.FormInputDonasi.route) {
            FormDonasiScreen(navController)
        }

        composable (route = Screen.SplashScreen.route){
            SplashScreen(navController)
        }

        composable (route= Screen.Profile.route){
            ProfileScreen(navController)
        }

        composable(
            route = Screen.DetailDonasi.route,
            arguments = listOf(navArgument("donasiId") { type = NavType.StringType })
        ) { backStackEntry ->
            val donasiId = backStackEntry.arguments?.getString("donasiId")
            if (!donasiId.isNullOrEmpty()) {
                DetailDonasiScreen(donasiId = donasiId, navController = navController)
            } else {
                Text("Donasi ID tidak ditemukan")
            }
        }

        composable(
            route = Screen.EditDonasi.route,
            arguments = listOf(navArgument("donasiId") {type = NavType.StringType})
        ) {backStackEntry ->
            val donasiId = backStackEntry.arguments?.getString("donasiId")
            if (!donasiId.isNullOrEmpty()) {
                FormDonasiScreen(donasiId = donasiId, navController = navController)
            } else {
                Text("Donasi ID tidak ditemukan")
            }
        }

        composable(
            route = Screen.HomeAdmin.route
        ) {
            AdminUserPengurusScreen(navController = navController)
        }
    }
}
