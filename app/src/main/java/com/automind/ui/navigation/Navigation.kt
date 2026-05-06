package com.automind.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.automind.ui.screens.DashboardScreen
import com.automind.ui.screens.InsightsScreen
import com.automind.ui.screens.RulesScreen
import com.automind.ui.screens.SettingsScreen
import com.automind.ui.theme.ElectricBlue
import com.automind.ui.theme.SurfaceDark
import com.automind.ui.viewmodel.AutoMindViewModel
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Login : Screen("login", "Login", Icons.Default.Lock)
    object Permissions : Screen("permissions", "Permissions", Icons.Default.Security)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Autopilot : Screen("autopilot", "Autopilot", Icons.Default.RocketLaunch)
    object Rules : Screen("rules", "Rules", Icons.Default.Rule)
    object Insights : Screen("insights", "Insights", Icons.Default.Analytics)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Autopilot,
    Screen.Rules,
    Screen.Insights,
    Screen.Settings
)

import com.automind.ui.screens.LoginScreen
import com.automind.ui.screens.AutopilotScreen
import com.automind.ui.screens.PermissionScreen
import androidx.compose.runtime.collectAsState

@Composable
fun AppNavigation(navController: NavHostController, viewModel: AutoMindViewModel) {
    val isAuth = FirebaseAuth.getInstance().currentUser != null
    val isPerms by viewModel.isPermissionsGranted.collectAsState()
    
    val startRoute = when {
        !isAuth -> Screen.Login.route
        !isPerms -> Screen.Permissions.route
        else -> Screen.Dashboard.route
    }
    
    NavHost(navController = navController, startDestination = startRoute) {
        composable(Screen.Login.route) { 
            LoginScreen(onLoginSuccess = {
                navController.navigate(if (isPerms) Screen.Dashboard.route else Screen.Permissions.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Permissions.route) {
            PermissionScreen(onAllGranted = {
                viewModel.checkPermissions()
                if (viewModel.isPermissionsGranted.value) {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Permissions.route) { inclusive = true }
                    }
                }
            })
        }
        composable(Screen.Dashboard.route) { DashboardScreen(viewModel) }
        composable(Screen.Autopilot.route) { AutopilotScreen(viewModel) }
        composable(Screen.Rules.route) { RulesScreen(viewModel) }
        composable(Screen.Insights.route) { InsightsScreen(viewModel) }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    if (currentRoute == Screen.Login.route || currentRoute == Screen.Permissions.route) return

    NavigationBar(
        containerColor = SurfaceDark
    ) {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ElectricBlue,
                    selectedTextColor = ElectricBlue,
                    unselectedIconColor = Color.White.copy(alpha = 0.5f),
                    unselectedTextColor = Color.White.copy(alpha = 0.5f),
                    indicatorColor = ElectricBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}
