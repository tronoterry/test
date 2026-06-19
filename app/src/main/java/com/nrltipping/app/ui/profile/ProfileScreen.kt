package com.nrltipping.app.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nrltipping.app.data.model.UserProfile

@Composable
fun ProfileScreen(
    profile: UserProfile,
    onSignOut: () -> Unit,
    onOpenAdmin: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineSmall)
        Text("Name: ${profile.displayName}")
        Text("Email: ${profile.email}")

        if (profile.isAdmin) {
            Button(onClick = onOpenAdmin, modifier = Modifier.fillMaxWidth()) {
                Text("Admin: manage rounds & results")
            }
        }

        OutlinedButton(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
            Text("Sign out")
        }
    }
}
