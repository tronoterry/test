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
import com.crf450rl.companion.data.ModsData
import com.crf450rl.companion.ui.components.ListEntryCard
import com.crf450rl.companion.ui.components.MenuTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModsListScreen(onOpenMenu: () -> Unit, onSelectMod: (String) -> Unit) {
    Scaffold(topBar = { MenuTopBar(title = "Mods & Upgrades", onOpenMenu = onOpenMenu) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ModsData.mods) { mod ->
                ListEntryCard(
                    icon = mod.icon,
                    title = mod.title,
                    subtitle = mod.summary,
                    trailingText = mod.category,
                    onClick = { onSelectMod(mod.id) }
                )
            }
        }
    }
}
