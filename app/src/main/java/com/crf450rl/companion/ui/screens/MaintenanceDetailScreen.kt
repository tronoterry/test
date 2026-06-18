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
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.crf450rl.companion.data.MaintenanceData
import com.crf450rl.companion.ui.components.BackTopBar
import com.crf450rl.companion.ui.components.DifficultyChip
import com.crf450rl.companion.ui.components.NumberedStepCard
import com.crf450rl.companion.ui.components.SectionHeader
import com.crf450rl.companion.ui.components.TimeChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceDetailScreen(guideId: String, onBack: () -> Unit) {
    val guide = MaintenanceData.guides.firstOrNull { it.id == guideId }
    Scaffold(topBar = { BackTopBar(title = guide?.title ?: "Guide", onBack = onBack) }) { padding ->
        if (guide == null) return@Scaffold
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(guide.summary, style = MaterialTheme.typography.bodyLarge)
                    Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DifficultyChip(guide.difficulty)
                        TimeChip(guide.timeEstimate)
                    }
                    guide.interval?.let {
                        Text(
                            "Interval: $it",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
            if (guide.toolsNeeded.isNotEmpty()) {
                item {
                    Column {
                        SectionHeader("Tools needed")
                        guide.toolsNeeded.forEach { tool ->
                            Row {
                                Icon(Icons.Outlined.Build, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                Text(tool, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
            if (guide.partsNeeded.isNotEmpty()) {
                item {
                    Column {
                        SectionHeader("Parts / supplies needed")
                        guide.partsNeeded.forEach { part ->
                            Row {
                                Icon(Icons.Outlined.Inventory2, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                Text(part, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
            guide.safetyNote?.let { note ->
                item {
                    Row {
                        Icon(Icons.Outlined.Warning, contentDescription = null, tint = Color(0xFFC62828), modifier = Modifier.padding(end = 8.dp))
                        Text(note, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFC62828))
                    }
                }
            }
            item { SectionHeader("Step-by-step") }
            itemsIndexed(guide.steps) { index, step ->
                NumberedStepCard(index = index + 1, step = step)
            }
        }
    }
}
