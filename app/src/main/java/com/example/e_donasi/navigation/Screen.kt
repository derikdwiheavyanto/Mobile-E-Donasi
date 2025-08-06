package com.example.e_donasi.navigation


sealed class Screen(val route: String) {


    object Home : Screen(route = "home")


    object Result : Screen(route = "result/{text}") {


        fun passText(text: String): String {
            return "result/$text"
        }
    }


    object Profile : Screen(route = "profile")


    object Login : Screen(route = "login")

    object Register : Screen(route = "register")

    object CreateNotes : Screen(route = "create_notes")

    object SplashScreen : Screen(route = "splash")

}
