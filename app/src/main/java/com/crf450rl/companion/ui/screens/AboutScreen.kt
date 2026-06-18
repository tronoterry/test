package com.crf450rl.companion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crf450rl.companion.ui.components.MenuTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onOpenMenu: () -> Unit) {
    Scaffold(topBar = { MenuTopBar(title = "About & Disclaimer", onOpenMenu = onOpenMenu) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("CRF450RL Companion", style = MaterialTheme.typography.titleLarge)
            Text(
                "An unofficial maintenance, troubleshooting, and modification reference for the Honda CRF450RL, " +
                    "built for owners who want clear, organized guides with original illustrative diagrams.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Not affiliated with Honda", style = MaterialTheme.typography.titleMedium)
            Text(
                "This app is an independent, unofficial reference. It is not produced, endorsed, or affiliated with " +
                    "Honda Motor Co., Ltd. or American Honda Motor Co., Inc. \"Honda\" and \"CRF450RL\" are used here " +
                    "solely to identify the motorcycle this app is written for.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Safety disclaimer", style = MaterialTheme.typography.titleMedium)
            Text(
                "Maintenance, troubleshooting, and modification information in this app is provided for general " +
                    "reference only and may not reflect the exact specifications for your specific model year or " +
                    "market. Always consult Honda's official owner's manual and service manual for authoritative " +
                    "torque values, fluid specifications, and procedures. Working on your own motorcycle carries " +
                    "inherent risk — if you are not confident performing a procedure safely, have it done by a " +
                    "qualified technician. Modifications, especially to emissions or safety-related systems, may " +
                    "affect street legality, warranty coverage, and insurance in your jurisdiction.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Diagrams", style = MaterialTheme.typography.titleMedium)
            Text(
                "All diagrams in this app are original schematic illustrations created for this app, not scans or " +
                    "reproductions of Honda's service literature.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
