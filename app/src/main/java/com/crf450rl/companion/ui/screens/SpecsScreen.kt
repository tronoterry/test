package com.crf450rl.companion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crf450rl.companion.data.SpecsData
import com.crf450rl.companion.ui.components.MenuTopBar
import com.crf450rl.companion.ui.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecsScreen(onOpenMenu: () -> Unit) {
    Scaffold(topBar = { MenuTopBar(title = "Specs Reference", onOpenMenu = onOpenMenu) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    SpecsData.DISCLAIMER,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            SpecsData.sections.forEach { section ->
                item {
                    Column {
                        SectionHeader(section.title)
                        Card(elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                section.rows.forEachIndexed { index, row ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                                    ) {
                                        Text(
                                            row.label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            row.value,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (index != section.rows.lastIndex) HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
