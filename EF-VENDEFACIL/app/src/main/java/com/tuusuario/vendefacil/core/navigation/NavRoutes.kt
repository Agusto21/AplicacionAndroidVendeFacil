package com.tuusuario.vendefacil.core.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object Dashboard : NavRoutes("dashboard")
    object Charge : NavRoutes("charge")
    object Success : NavRoutes("success/{amount}") {
        fun createRoute(amount: Double) = "success/$amount"
    }
    object Products : NavRoutes("products")
    object History : NavRoutes("history")
    object Profile : NavRoutes("profile")

    object Notifications : NavRoutes("notifications")
    object Security : NavRoutes("security")
    object HelpSupport : NavRoutes("help_support")
}