package com.crf450rl.companion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crf450rl.companion.data.Icons2
import com.crf450rl.companion.navigation.Routes
import com.crf450rl.companion.ui.components.ListEntryCard
import com.crf450rl.companion.ui.components.MenuTopBar

private data class HomeTile(val title: String, val subtitle: String, val route: String)

private val homeTiles = listOf(
    HomeTile("Maintenance", "Oil, chain, valves, filters and every routine service job, with step-by-step guides.", Routes.MAINTENANCE_LIST),
    HomeTile("Troubleshooting", "Diagnose common starting, running, electrical, and handling problems.", Routes.TROUBLESHOOTING_LIST),
    HomeTile("Mods & Upgrades", "Exhaust, ECU tuning, suspension, gearing, protection, and cosmetic mods.", Routes.MODS_LIST),
    HomeTile("Specs Reference", "Quick-reference engine, chassis, and service specifications.", Routes.SPECS)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onOpenMenu: () -> Unit, onNavigate: (String) -> Unit) {
    Scaffold(topBar = { MenuTopBar(title = "2025 Honda CRF450RL", onOpenMenu = onOpenMenu) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(
                        "Maintenance, Troubleshooting & Mods Companion",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "Everything you need to keep your CRF450RL running and dialed in, organized into clear guides with original diagrams.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                }
            }
            items(homeTiles) { tile ->
                ListEntryCard(
                    icon = when (tile.route) {
                        Routes.MAINTENANCE_LIST -> Icons2.Maintenance
                        Routes.TROUBLESHOOTING_LIST -> Icons2.Troubleshooting
                        Routes.MODS_LIST -> Icons2.Mods
                        else -> Icons2.Specs
                    },
                    title = tile.title,
                    subtitle = tile.subtitle,
                    onClick = { onNavigate(tile.route) }
                )
            }
        }
    }
}
