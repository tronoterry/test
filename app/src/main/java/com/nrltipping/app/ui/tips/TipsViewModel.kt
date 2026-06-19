package com.nrltipping.app.ui.tips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrltipping.app.data.model.Game
import com.nrltipping.app.data.model.Pick
import com.nrltipping.app.data.model.Round
import com.nrltipping.app.data.repository.AuthRepository
import com.nrltipping.app.data.repository.RoundRepository
import com.nrltipping.app.data.repository.ServiceLocator
import com.nrltipping.app.data.repository.TipRepository
import com.nrltipping.app.util.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

data class TipsUiState(
    val isLoading: Boolean = true,
    val round: Round? = null,
    val games: List<Game> = emptyList(),
    val picks: Map<String, Pick> = emptyMap(),
    val correctCount: Int? = null,
    val isSaving: Boolean = false,
    val saveMessage: String? = null,
    val error: String? = null,
)

class TipsViewModel(
    private val roundRepository: RoundRepository = ServiceLocator.roundRepository,
    private val tipRepository: TipRepository = ServiceLocator.tipRepository,
    private val authRepository: AuthRepository = ServiceLocator.authRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TipsUiState())
    val uiState: StateFlow<TipsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        val uid = authRepository.currentUserId ?: return
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            runCatching {
                val season = TimeUtils.currentSeason()
                val round = roundRepository.getCurrentRound(season)
                val games = round?.let { roundRepository.getGamesForRound(season, it.round) } ?: emptyList()
                val existingTip = round?.let { tipRepository.getTip(season, it.round, uid) }
                val picks = existingTip?.picks?.mapNotNull { (gameId, pickName) ->
                    runCatching { Pick.valueOf(pickName) }.getOrNull()?.let { gameId to it }
                }?.toMap() ?: emptyMap()
                Triple(round, games, picks) to existingTip?.correctCount
            }.onSuccess { (data, correctCount) ->
                val (round, games, picks) = data
                _uiState.value = TipsUiState(
                    isLoading = false,
                    round = round,
                    games = games,
                    picks = picks,
                    correctCount = correctCount,
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Failed to load round")
            }
        }
    }

    fun selectPick(gameId: String, pick: Pick) {
        val current = _uiState.value
        if (isLocked(current.round, Date())) return
        _uiState.value = current.copy(picks = current.picks + (gameId to pick), saveMessage = null)
    }

    fun isLocked(round: Round?, now: Date = Date()): Boolean = TimeUtils.isLocked(round?.lockTime, now)

    fun submit() {
        val state = _uiState.value
        val round = state.round ?: return
        val uid = authRepository.currentUserId ?: return
        if (isLocked(round)) {
            _uiState.value = state.copy(error = "Tipping is locked for this round")
            return
        }
        _uiState.value = state.copy(isSaving = true, error = null)
        viewModelScope.launch {
            runCatching {
                tipRepository.submitTip(round.season, round.round, uid, state.picks)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(isSaving = false, saveMessage = "Tips saved")
            }.onFailure {
                _uiState.value = _uiState.value.copy(isSaving = false, error = it.message ?: "Failed to save tips")
            }
        }
    }
}
