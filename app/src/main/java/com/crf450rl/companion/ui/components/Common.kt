package com.crf450rl.companion.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.crf450rl.companion.data.Difficulty
import com.crf450rl.companion.data.GuideStep
import com.crf450rl.companion.ui.diagrams.DiagramView

@Composable
fun ListEntryCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp).size(28.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                if (trailingText != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        trailingText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun DifficultyChip(difficulty: Difficulty) {
    val color = when (difficulty) {
        Difficulty.EASY -> Color(0xFF2E7D32)
        Difficulty.MODERATE -> Color(0xFFEF6C00)
        Difficulty.ADVANCED -> Color(0xFFC62828)
    }
    AssistChip(
        onClick = {},
        enabled = false,
        label = { Text(difficulty.label) },
        leadingIcon = { Icon(Icons.Outlined.Build, contentDescription = null, modifier = Modifier.size(16.dp)) },
        colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
            disabledLabelColor = color,
            disabledLeadingIconContentColor = color
        )
    )
}

@Composable
fun InfoChip(icon: ImageVector, text: String) {
    AssistChip(
        onClick = {},
        enabled = false,
        label = { Text(text) },
        leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp)) }
    )
}

@Composable
fun NumberedStepCard(index: Int, step: GuideStep) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "$index",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(step.title, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(10.dp))
            Text(step.description, style = MaterialTheme.typography.bodyMedium)

            step.diagram?.let { diagram ->
                Spacer(Modifier.height(12.dp))
                DiagramView(type = diagram)
            }

            step.tip?.let { tip ->
                Spacer(Modifier.height(10.dp))
                NoteRow(icon = Icons.Outlined.Info, text = tip, color = MaterialTheme.colorScheme.primary)
            }
            step.warning?.let { warning ->
                Spacer(Modifier.height(10.dp))
                NoteRow(icon = Icons.Outlined.Construction, text = warning, color = Color(0xFFC62828))
            }
        }
    }
}

@Composable
private fun NoteRow(icon: ImageVector, text: String, color: Color) {
    Row {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = color)
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun TimeChip(text: String) = InfoChip(Icons.Outlined.Schedule, text)
