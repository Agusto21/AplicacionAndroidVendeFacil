package com.tuusuario.vendefacil.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.tuusuario.vendefacil.di.AppContainer
import com.tuusuario.vendefacil.presentation.screens.auth.*
import com.tuusuario.vendefacil.presentation.screens.charge.*
import com.tuusuario.vendefacil.presentation.screens.dashboard.DashboardScreen
import com.tuusuario.vendefacil.presentation.screens.history.HistoryScreen
import com.tuusuario.vendefacil.presentation.screens.products.ProductsScreen
import com.tuusuario.vendefacil.presentation.screens.profile.*
import com.tuusuario.vendefacil.presentation.screens.splash.SplashScreen

@Composable
fun AppNavigation(appContainer: AppContainer) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoutes.Splash.route){
        composable(NavRoutes.Splash.route) {
            SplashScreen(onNavigateToLogin = {
                navController.navigate(NavRoutes.Login.route) { popUpTo(0)}
            })
        }
        composable(NavRoutes.Login.route) {
            LoginScreen(
                appContainer = appContainer,
                onNavigateToDashboard = { navController.navigate(NavRoutes.Dashboard.route) { popUpTo(0) }},
                onNavigateToRegister = { navController.navigate(NavRoutes.Register.route) }
            )
        }
        composable(NavRoutes.Register.route) {
            RegisterScreen(
                appContainer = appContainer,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.Dashboard.route) {
            DashboardScreen(
                appContainer = appContainer,
                onNavigateToCharge = { navController.navigate(NavRoutes.Charge.route) },
                onNavigateToProducts = { navController.navigate(NavRoutes.Products.route) },
                onNavigateToHistory = { navController.navigate(NavRoutes.History.route) },
                onNavigateToProfile = { navController.navigate(NavRoutes.Profile.route) }
            )
        }
        composable(NavRoutes.Charge.route) {
            ChargeScreen(
                appContainer = appContainer,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSuccess = { amount ->
                    navController.navigate(NavRoutes.Success.createRoute(amount)) {
                        popUpTo(NavRoutes.Charge.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = NavRoutes.Success.route,
            arguments = listOf(navArgument("amount") { type = NavType.FloatType })
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 0.0
            SuccessScreen(
                amount = amount,
                onNavigateToDashboard = { navController.navigate(NavRoutes.Dashboard.route) { popUpTo(NavRoutes.Dashboard.route) { inclusive = true } } }
            )
        }
        composable(NavRoutes.Products.route) {
            ProductsScreen(appContainer = appContainer, onNavigateBack = { navController.popBackStack() })
        }
        composable(NavRoutes.History.route) {
            HistoryScreen(appContainer = appContainer, onNavigateBack = { navController.popBackStack() })
        }

        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                appContainer = appContainer,
                onNavigateBack = { navController.popBackStack() },
                onLogout = { navController.navigate(NavRoutes.Login.route) { popUpTo(0) } },
                onNavigateToNotifications = { navController.navigate(NavRoutes.Notifications.route) },
                onNavigateToSecurity = { navController.navigate(NavRoutes.Security.route) },
                onNavigateToHelp = { navController.navigate(NavRoutes.HelpSupport.route) }
            )
        }

        composable(NavRoutes.Notifications.route) {
            NotificationsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(NavRoutes.Security.route) {
            SecurityScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(NavRoutes.HelpSupport.route) {
            HelpSupportScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}