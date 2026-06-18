package com.crf450rl.companion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crf450rl.companion.data.ModsData
import com.crf450rl.companion.ui.components.BackTopBar
import com.crf450rl.companion.ui.components.DifficultyChip
import com.crf450rl.companion.ui.components.NumberedStepCard
import com.crf450rl.companion.ui.components.SectionHeader
import com.crf450rl.companion.ui.components.TimeChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModsDetailScreen(modId: String, onBack: () -> Unit) {
    val mod = ModsData.mods.firstOrNull { it.id == modId }
    Scaffold(topBar = { BackTopBar(title = mod?.title ?: "Mod", onBack = onBack) }) { padding ->
        if (mod == null) return@Scaffold
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(mod.summary, style = MaterialTheme.typography.bodyLarge)
                    Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DifficultyChip(mod.difficulty)
                        TimeChip(mod.timeEstimate)
                    }
                }
            }
            mod.legalNote?.let { note ->
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Outlined.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            Text(
                                note,
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
            if (mod.toolsNeeded.isNotEmpty()) {
                item {
                    Column {
                        SectionHeader("Tools needed")
                        mod.toolsNeeded.forEach { tool ->
                            Row {
                                Icon(Icons.Outlined.Build, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                Text(tool, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
            if (mod.partsNeeded.isNotEmpty()) {
                item {
                    Column {
                        SectionHeader("Parts needed")
                        mod.partsNeeded.forEach { part ->
                            Row {
                                Icon(Icons.Outlined.Inventory2, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                Text(part, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
            item { SectionHeader("Step-by-step") }
            itemsIndexed(mod.steps) { index, step ->
                NumberedStepCard(index = index + 1, step = step)
            }
        }
    }
}
