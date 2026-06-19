package com.nrltipping.app.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrltipping.app.data.model.Game
import com.nrltipping.app.data.repository.RoundRepository
import com.nrltipping.app.data.repository.ServiceLocator
import com.nrltipping.app.data.repository.TipRepository
import com.nrltipping.app.util.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

data class AdminUiState(
    val round: Int = 1,
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

class AdminViewModel(
    private val roundRepository: RoundRepository = ServiceLocator.roundRepository,
    private val tipRepository: TipRepository = ServiceLocator.tipRepository,
) : ViewModel() {

    private val season = TimeUtils.currentSeason()
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        loadGames()
    }

    fun setRound(round: Int) {
        _uiState.value = _uiState.value.copy(round = round)
        loadGames()
    }

    fun loadGames() {
        val round = _uiState.value.round
        viewModelScope.launch {
            runCatching { roundRepository.getGamesForRound(season, round) }
                .onSuccess { _uiState.value = _uiState.value.copy(games = it) }
                .onFailure { _uiState.value = _uiState.value.copy(error = it.message) }
        }
    }

    fun addGame(homeTeam: String, awayTeam: String, kickoff: Date, homeOdds: Double, awayOdds: Double) {
        val round = _uiState.value.round
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching {
                roundRepository.addGame(season, round, homeTeam, awayTeam, kickoff, homeOdds, awayOdds)
            }.onSuccess {
                loadGames()
                _uiState.value = _uiState.value.copy(isLoading = false, message = "Game added")
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Failed to add game")
            }
        }
    }

    fun submitResult(gameId: String, homeScore: Int, awayScore: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching { roundRepository.submitResult(gameId, homeScore, awayScore) }
                .onSuccess {
                    loadGames()
                    _uiState.value = _uiState.value.copy(isLoading = false, message = "Result saved")
                }
                .onFailure { _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Failed to save result") }
        }
    }

    fun finalizeRound() {
        val round = _uiState.value.round
        val games = _uiState.value.games
        if (games.any { it.winner == null }) {
            _uiState.value = _uiState.value.copy(error = "All games need a final score before finalizing")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching { tipRepository.finalizeRound(season, round, games) }
                .onSuccess { _uiState.value = _uiState.value.copy(isLoading = false, message = "Round $round finalized") }
                .onFailure { _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Failed to finalize round") }
        }
    }
}
