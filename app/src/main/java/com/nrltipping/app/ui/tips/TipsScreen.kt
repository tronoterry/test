package com.nrltipping.app.ui.tips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nrltipping.app.data.model.Game
import com.nrltipping.app.data.model.Pick
import com.nrltipping.app.util.TimeUtils
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun TipsScreen(viewModel: TipsViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var now by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = Date()
            delay(1000)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            state.round == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No round is currently open for tipping.")
            }
            else -> {
                val locked = viewModel.isLocked(state.round, now)
                val lockTime = state.round?.lockTime

                Text("Round ${state.round?.round}", style = MaterialTheme.typography.headlineSmall)
                if (lockTime != null) {
                    Text(
                        if (locked) "Tipping locked" else "Locks in ${TimeUtils.formatCountdown(lockTime, now)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (locked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    )
                }
                state.correctCount?.let {
                    Text("Result: $it/${state.games.size} correct", style = MaterialTheme.typography.bodyMedium)
                }

                LazyColumn(
                    modifier = Modifier.weight(1f).padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.games) { game ->
                        GameTipCard(
                            game = game,
                            selectedPick = state.picks[game.id],
                            locked = locked,
                            onPick = { viewModel.selectPick(game.id, it) },
                        )
                    }
                }

                state.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }
                state.saveMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
                }

                Button(
                    onClick = { viewModel.submit() },
                    enabled = !locked && !state.isSaving && state.picks.size == state.games.size,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                ) {
                    Text(if (state.isSaving) "Saving..." else "Save tips")
                }
            }
        }
    }
}

@Composable
private fun GameTipCard(
    game: Game,
    selectedPick: Pick?,
    locked: Boolean,
    onPick: (Pick) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(TimeUtils.formatDateTime(game.kickoff), style = MaterialTheme.typography.bodySmall)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TeamPickButton(
                    label = game.homeTeam,
                    odds = game.homeOdds,
                    selected = selectedPick == Pick.HOME,
                    enabled = !locked,
                    onClick = { onPick(Pick.HOME) },
                    modifier = Modifier.weight(1f),
                )
                TeamPickButton(
                    label = game.awayTeam,
                    odds = game.awayOdds,
                    selected = selectedPick == Pick.AWAY,
                    enabled = !locked,
                    onClick = { onPick(Pick.AWAY) },
                    modifier = Modifier.weight(1f),
                )
            }
            if (game.homeScore != null && game.awayScore != null) {
                Text(
                    "Final: ${game.homeTeam} ${game.homeScore} - ${game.awayScore} ${game.awayTeam}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun TeamPickButton(
    label: String,
    odds: Double,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val content: @Composable () -> Unit = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label)
            Text("$odds", style = MaterialTheme.typography.bodySmall)
        }
    }
    if (selected) {
        Button(onClick = onClick, enabled = enabled, modifier = modifier) { content() }
    } else {
        OutlinedButton(onClick = onClick, enabled = enabled, modifier = modifier) { content() }
    }
}
