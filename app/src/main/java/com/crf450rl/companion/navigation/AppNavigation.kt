package com.crf450rl.companion.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crf450rl.companion.ui.screens.AboutScreen
import com.crf450rl.companion.ui.screens.HomeScreen
import com.crf450rl.companion.ui.screens.MaintenanceDetailScreen
import com.crf450rl.companion.ui.screens.MaintenanceListScreen
import com.crf450rl.companion.ui.screens.ModsDetailScreen
import com.crf450rl.companion.ui.screens.ModsListScreen
import com.crf450rl.companion.ui.screens.SpecsScreen
import com.crf450rl.companion.ui.screens.TroubleshootingDetailScreen
import com.crf450rl.companion.ui.screens.TroubleshootingListScreen
import kotlinx.coroutines.launch

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "CRF450RL Companion",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(20.dp)
                )
                Column {
                    drawerDestinations.forEach { destination ->
                        NavigationDrawerItem(
                            label = { Text(destination.label) },
                            selected = currentRoute == destination.route,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(destination.route) {
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Routes.HOME) {
            composable(Routes.HOME) {
                HomeScreen(
                    onOpenMenu = { scope.launch { drawerState.open() } },
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
            composable(Routes.MAINTENANCE_LIST) {
                MaintenanceListScreen(
                    onOpenMenu = { scope.launch { drawerState.open() } },
                    onSelectGuide = { id -> navController.navigate(Routes.maintenanceDetail(id)) }
                )
            }
            composable(
                route = Routes.MAINTENANCE_DETAIL,
                arguments = listOf(navArgument("guideId") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("guideId").orEmpty()
                MaintenanceDetailScreen(guideId = id, onBack = { navController.popBackStack() })
            }
            composable(Routes.TROUBLESHOOTING_LIST) {
                TroubleshootingListScreen(
                    onOpenMenu = { scope.launch { drawerState.open() } },
                    onSelectIssue = { id -> navController.navigate(Routes.troubleshootingDetail(id)) }
                )
            }
            composable(
                route = Routes.TROUBLESHOOTING_DETAIL,
                arguments = listOf(navArgument("issueId") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("issueId").orEmpty()
                TroubleshootingDetailScreen(issueId = id, onBack = { navController.popBackStack() })
            }
            composable(Routes.MODS_LIST) {
                ModsListScreen(
                    onOpenMenu = { scope.launch { drawerState.open() } },
                    onSelectMod = { id -> navController.navigate(Routes.modsDetail(id)) }
                )
            }
            composable(
                route = Routes.MODS_DETAIL,
                arguments = listOf(navArgument("modId") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("modId").orEmpty()
                ModsDetailScreen(modId = id, onBack = { navController.popBackStack() })
            }
            composable(Routes.SPECS) {
                SpecsScreen(onOpenMenu = { scope.launch { drawerState.open() } })
            }
            composable(Routes.ABOUT) {
                AboutScreen(onOpenMenu = { scope.launch { drawerState.open() } })
            }
        }
    }
}
