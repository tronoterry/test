package com.nrltipping.app.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/** Pick a team for a game. */
enum class Pick { HOME, AWAY }

/** Outcome of a finished game. DRAW is rare in NRL but possible. */
enum class Winner { HOME, AWAY, DRAW }

enum class GameStatus { SCHEDULED, FINAL }

data class UserProfile(
    @DocumentId val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val isAdmin: Boolean = false,
    @ServerTimestamp val createdAt: Date? = null,
)

/**
 * Stored at top level as "{season}_{round}" so games for a round can be
 * queried directly and the round's lock time can be checked by security rules.
 */
data class Round(
    @DocumentId val id: String = "",
    val season: Int = 0,
    val round: Int = 0,
    val lockTime: Date? = null,
    val finalized: Boolean = false,
)

data class Game(
    @DocumentId val id: String = "",
    val season: Int = 0,
    val round: Int = 0,
    val homeTeam: String = "",
    val awayTeam: String = "",
    val kickoff: Date? = null,
    val homeOdds: Double = 0.0,
    val awayOdds: Double = 0.0,
    val homeScore: Int? = null,
    val awayScore: Int? = null,
    val status: GameStatus = GameStatus.SCHEDULED,
) {
    @get:Exclude
    val winner: Winner?
        get() {
            val h = homeScore ?: return null
            val a = awayScore ?: return null
            return when {
                h > a -> Winner.HOME
                a > h -> Winner.AWAY
                else -> Winner.DRAW
            }
        }
}

/** One user's set of picks for a single round. Document id = "{season}_{round}_{uid}". */
data class Tip(
    @DocumentId val id: String = "",
    val uid: String = "",
    val season: Int = 0,
    val round: Int = 0,
    val picks: Map<String, String> = emptyMap(), // gameId -> Pick.name
    @ServerTimestamp val updatedAt: Date? = null,
    val correctCount: Int? = null,
    val scored: Boolean = false,
)

/** Denormalized weekly result row, queried by season+round for the weekly leaderboard. */
data class RoundResult(
    @DocumentId val id: String = "",
    val uid: String = "",
    val displayName: String = "",
    val season: Int = 0,
    val round: Int = 0,
    val correct: Int = 0,
    val totalGames: Int = 0,
)

/** Denormalized season-to-date totals, queried by season for the season leaderboard. */
data class SeasonStanding(
    @DocumentId val id: String = "",
    val uid: String = "",
    val displayName: String = "",
    val season: Int = 0,
    val totalCorrect: Int = 0,
    val roundsScored: Int = 0,
)
