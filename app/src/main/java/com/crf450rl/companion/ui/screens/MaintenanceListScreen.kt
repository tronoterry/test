package com.crf450rl.companion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crf450rl.companion.data.MaintenanceData
import com.crf450rl.companion.ui.components.ListEntryCard
import com.crf450rl.companion.ui.components.MenuTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceListScreen(onOpenMenu: () -> Unit, onSelectGuide: (String) -> Unit) {
    Scaffold(topBar = { MenuTopBar(title = "Maintenance", onOpenMenu = onOpenMenu) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MaintenanceData.guides) { guide ->
                ListEntryCard(
                    icon = guide.icon,
                    title = guide.title,
                    subtitle = guide.summary,
                    trailingText = guide.interval?.let { "Interval: $it" },
                    onClick = { onSelectGuide(guide.id) }
                )
            }
        }
    }
}
