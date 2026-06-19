package com.nrltipping.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nrltipping.app.ui.admin.AdminScreen
import com.nrltipping.app.ui.auth.AuthScreen
import com.nrltipping.app.ui.auth.AuthState
import com.nrltipping.app.ui.auth.AuthViewModel
import com.nrltipping.app.ui.leaderboard.LeaderboardScreen
import com.nrltipping.app.ui.profile.ProfileScreen
import com.nrltipping.app.ui.tips.TipsScreen

private object Routes {
    const val TIPS = "tips"
    const val LEADERBOARD = "leaderboard"
    const val PROFILE = "profile"
    const val ADMIN = "admin"
}

@Composable
fun NrlNavGraph(authViewModel: AuthViewModel = viewModel()) {
    val state by authViewModel.state.collectAsState()

    when (val s = state) {
        is AuthState.SignedIn -> MainScaffold(authViewModel, s)
        is AuthState.Error -> AuthScreen()
        AuthState.Loading -> AuthScreen()
        AuthState.SignedOut -> AuthScreen()
    }
}

@Composable
private fun MainScaffold(authViewModel: AuthViewModel, signedIn: AuthState.SignedIn) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.hierarchy?.firstOrNull()?.route

                NavigationBarItem(
                    selected = currentRoute == Routes.TIPS,
                    onClick = { navController.navigate(Routes.TIPS) },
                    icon = { Icon(Icons.Filled.Checklist, contentDescription = "Tips") },
                    label = { Text("Tips") },
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.LEADERBOARD,
                    onClick = { navController.navigate(Routes.LEADERBOARD) },
                    icon = { Icon(Icons.Filled.EmojiEvents, contentDescription = "Leaderboard") },
                    label = { Text("Leaderboard") },
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.PROFILE,
                    onClick = { navController.navigate(Routes.PROFILE) },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                )
                if (signedIn.profile.isAdmin) {
                    NavigationBarItem(
                        selected = currentRoute == Routes.ADMIN,
                        onClick = { navController.navigate(Routes.ADMIN) },
                        icon = { Icon(Icons.Filled.AdminPanelSettings, contentDescription = "Admin") },
                        label = { Text("Admin") },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.TIPS,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.TIPS) { TipsScreen() }
            composable(Routes.LEADERBOARD) { LeaderboardScreen() }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    profile = signedIn.profile,
                    onSignOut = { authViewModel.signOut() },
                    onOpenAdmin = { navController.navigate(Routes.ADMIN) },
                )
            }
            composable(Routes.ADMIN) { AdminScreen() }
        }
    }
}
