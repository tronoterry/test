package com.crf450rl.companion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FiberManualRecord
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crf450rl.companion.data.TroubleshootingData
import com.crf450rl.companion.ui.components.BackTopBar
import com.crf450rl.companion.ui.components.NumberedStepCard
import com.crf450rl.companion.ui.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TroubleshootingDetailScreen(issueId: String, onBack: () -> Unit) {
    val issue = TroubleshootingData.issues.firstOrNull { it.id == issueId }
    Scaffold(topBar = { BackTopBar(title = issue?.title ?: "Issue", onBack = onBack) }) { padding ->
        if (issue == null) return@Scaffold
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(issue.summary, style = MaterialTheme.typography.bodyLarge)
                    SectionHeader("Common symptoms")
                    issue.symptoms.forEach { symptom ->
                        Row {
                            Icon(
                                Icons.Outlined.FiberManualRecord,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp, top = 4.dp)
                            )
                            Text(symptom, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            item { SectionHeader("Diagnostic steps") }
            itemsIndexed(issue.steps) { index, step ->
                NumberedStepCard(index = index + 1, step = step)
            }
        }
    }
}
