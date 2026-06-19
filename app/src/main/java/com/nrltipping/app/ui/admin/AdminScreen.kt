package com.nrltipping.app.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nrltipping.app.data.model.Game
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Admin-only screen. Round number, fixtures and betting odds are entered manually here
 * because there is no NRL fixtures/odds API key configured. Swap [AdminViewModel]'s
 * repository calls for a real feed when one is available - the rest of the app only
 * depends on the `games`/`rounds` Firestore collections, not on how they're populated.
 */
@Composable
fun AdminScreen(viewModel: AdminViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var roundText by remember { mutableStateOf(state.round.toString()) }
    var homeTeam by remember { mutableStateOf("") }
    var awayTeam by remember { mutableStateOf("") }
    var kickoffDate by remember { mutableStateOf("") }
    var kickoffTime by remember { mutableStateOf("") }
    var homeOdds by remember { mutableStateOf("") }
    var awayOdds by remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Admin", style = MaterialTheme.typography.headlineSmall)
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                OutlinedTextField(
                    value = roundText,
                    onValueChange = { roundText = it },
                    label = { Text("Round number") },
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = { roundText.toIntOrNull()?.let(viewModel::setRound) },
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text("Load") }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add fixture", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(value = homeTeam, onValueChange = { homeTeam = it }, label = { Text("Home team") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = awayTeam, onValueChange = { awayTeam = it }, label = { Text("Away team") }, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = kickoffDate, onValueChange = { kickoffDate = it }, label = { Text("Date yyyy-MM-dd") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = kickoffTime, onValueChange = { kickoffTime = it }, label = { Text("Time HH:mm") }, modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = homeOdds, onValueChange = { homeOdds = it }, label = { Text("Home odds") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = awayOdds, onValueChange = { awayOdds = it }, label = { Text("Away odds") }, modifier = Modifier.weight(1f))
                    }
                    Button(
                        onClick = {
                            val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            val kickoff = runCatching { fmt.parse("$kickoffDate $kickoffTime") }.getOrNull()
                            val home = homeOdds.toDoubleOrNull()
                            val away = awayOdds.toDoubleOrNull()
                            if (kickoff != null && home != null && away != null && homeTeam.isNotBlank() && awayTeam.isNotBlank()) {
                                viewModel.addGame(homeTeam, awayTeam, kickoff, home, away)
                                homeTeam = ""; awayTeam = ""; kickoffDate = ""; kickoffTime = ""; homeOdds = ""; awayOdds = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Add game") }
                }
            }
        }

        items(state.games) { game ->
            GameResultCard(game = game, onSubmitResult = { h, a -> viewModel.submitResult(game.id, h, a) })
        }

        item {
            Button(onClick = { viewModel.finalizeRound() }, modifier = Modifier.fillMaxWidth()) {
                Text("Finalize round ${state.round} & update leaderboards")
            }
            state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable
private fun GameResultCard(game: Game, onSubmitResult: (Int, Int) -> Unit) {
    var homeScore by remember(game.id) { mutableStateOf(game.homeScore?.toString() ?: "") }
    var awayScore by remember(game.id) { mutableStateOf(game.awayScore?.toString() ?: "") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("${game.homeTeam} vs ${game.awayTeam}", style = MaterialTheme.typography.titleSmall)
            Text("Odds: ${game.homeOdds} / ${game.awayOdds}", style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = homeScore, onValueChange = { homeScore = it }, label = { Text("Home score") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = awayScore, onValueChange = { awayScore = it }, label = { Text("Away score") }, modifier = Modifier.weight(1f))
            }
            Button(
                onClick = {
                    val h = homeScore.toIntOrNull()
                    val a = awayScore.toIntOrNull()
                    if (h != null && a != null) onSubmitResult(h, a)
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Save result") }
        }
    }
}
