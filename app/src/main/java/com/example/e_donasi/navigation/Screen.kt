package com.example.e_donasi.navigation

import android.util.Log


sealed class Screen(val route: String) {


    object Home : Screen(route = "home")

    object Profile : Screen(route = "profile")

    object DetailDonasi : Screen(route = "detail_donasi/{donasiId}") {
        fun createRoute(donasiId: String): String {
            Log.d("KLIKNAV", donasiId)
            return "detail_donasi/$donasiId"
        }
    }

    object EditDonasi: Screen(route = "edit_donasi/{donasiId}"){
        fun createRoute(donasiId: String): String = "edit_donasi/$donasiId"
    }

    object FormInputDonasi : Screen(route = "form_input_donasi")


    object  HomeAdmin : Screen(route = "home_admin")


    object Login : Screen(route = "login")

    object Register : Screen(route = "register")


    object SplashScreen : Screen(route = "splash")

}
