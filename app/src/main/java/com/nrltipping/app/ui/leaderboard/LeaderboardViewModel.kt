package com.nrltipping.app.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrltipping.app.data.model.RoundResult
import com.nrltipping.app.data.model.SeasonStanding
import com.nrltipping.app.data.repository.RoundRepository
import com.nrltipping.app.data.repository.ServiceLocator
import com.nrltipping.app.data.repository.TipRepository
import com.nrltipping.app.util.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LeaderboardUiState(
    val isLoading: Boolean = true,
    val availableRounds: List<Int> = emptyList(),
    val selectedRound: Int? = null,
    val weekly: List<RoundResult> = emptyList(),
    val season: List<SeasonStanding> = emptyList(),
    val error: String? = null,
)

class LeaderboardViewModel(
    private val roundRepository: RoundRepository = ServiceLocator.roundRepository,
    private val tipRepository: TipRepository = ServiceLocator.tipRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    private val season = TimeUtils.currentSeason()

    init {
        load()
    }

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching {
                val rounds = roundRepository.listRounds(season).map { it.round }
                val latestRound = rounds.maxOrNull()
                val weekly = latestRound?.let { tipRepository.getWeeklyLeaderboard(season, it) } ?: emptyList()
                val standings = tipRepository.getSeasonLeaderboard(season)
                Triple(rounds, latestRound, weekly) to standings
            }.onSuccess { (data, standings) ->
                val (rounds, latestRound, weekly) = data
                _uiState.value = LeaderboardUiState(
                    isLoading = false,
                    availableRounds = rounds,
                    selectedRound = latestRound,
                    weekly = weekly,
                    season = standings,
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Failed to load leaderboard")
            }
        }
    }

    fun selectRound(round: Int) {
        _uiState.value = _uiState.value.copy(selectedRound = round, isLoading = true)
        viewModelScope.launch {
            runCatching { tipRepository.getWeeklyLeaderboard(season, round) }
                .onSuccess { _uiState.value = _uiState.value.copy(isLoading = false, weekly = it) }
                .onFailure { _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Failed to load round") }
        }
    }
}
