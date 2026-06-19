package com.nrltipping.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nrltipping.app.data.model.Game
import com.nrltipping.app.data.model.GameStatus
import com.nrltipping.app.data.model.Round
import kotlinx.coroutines.tasks.await
import java.util.Date

/** Reads/writes rounds and games. Round id and game ordering keys are "{season}_{round}". */
class RoundRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    private fun roundDocId(season: Int, round: Int) = "${season}_${round}"

    suspend fun getRound(season: Int, round: Int): Round? {
        val snap = firestore.collection("rounds").document(roundDocId(season, round)).get().await()
        return snap.toObject(Round::class.java)
    }

    /** The most recent round that has not been finalized yet, or null if none scheduled. */
    suspend fun getCurrentRound(season: Int): Round? {
        val snap = firestore.collection("rounds")
            .whereEqualTo("season", season)
            .whereEqualTo("finalized", false)
            .orderBy("round", Query.Direction.ASCENDING)
            .limit(1)
            .get()
            .await()
        return snap.documents.firstOrNull()?.toObject(Round::class.java)
    }

    suspend fun getGamesForRound(season: Int, round: Int): List<Game> {
        val snap = firestore.collection("games")
            .whereEqualTo("season", season)
            .whereEqualTo("round", round)
            .orderBy("kickoff", Query.Direction.ASCENDING)
            .get()
            .await()
        return snap.documents.mapNotNull { it.toObject(Game::class.java) }
    }

    suspend fun listRounds(season: Int): List<Round> {
        val snap = firestore.collection("rounds")
            .whereEqualTo("season", season)
            .orderBy("round", Query.Direction.DESCENDING)
            .get()
            .await()
        return snap.documents.mapNotNull { it.toObject(Round::class.java) }
    }

    // ---- Admin operations ----

    /** Creates or updates a round. Lock time is always derived from the earliest game kickoff minus 1 hour. */
    suspend fun upsertRound(season: Int, round: Int, lockTime: Date?) {
        val data = mapOf(
            "season" to season,
            "round" to round,
            "lockTime" to lockTime,
            "finalized" to false,
        )
        firestore.collection("rounds").document(roundDocId(season, round))
            .set(data, com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    suspend fun addGame(
        season: Int,
        round: Int,
        homeTeam: String,
        awayTeam: String,
        kickoff: Date,
        homeOdds: Double,
        awayOdds: Double,
    ) {
        val game = Game(
            season = season,
            round = round,
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            kickoff = kickoff,
            homeOdds = homeOdds,
            awayOdds = awayOdds,
        )
        firestore.collection("games").add(game).await()
        recalculateLockTime(season, round)
    }

    suspend fun updateOdds(gameId: String, homeOdds: Double, awayOdds: Double) {
        firestore.collection("games").document(gameId)
            .update("homeOdds", homeOdds, "awayOdds", awayOdds)
            .await()
    }

    suspend fun submitResult(gameId: String, homeScore: Int, awayScore: Int) {
        firestore.collection("games").document(gameId)
            .update(
                "homeScore", homeScore,
                "awayScore", awayScore,
                "status", GameStatus.FINAL.name,
            )
            .await()
    }

    /** Lock time = kickoff of the earliest game in the round, minus 1 hour. */
    private suspend fun recalculateLockTime(season: Int, round: Int) {
        val games = getGamesForRound(season, round)
        val earliest = games.mapNotNull { it.kickoff }.minOrNull() ?: return
        val lockTime = Date(earliest.time - 60 * 60 * 1000L)
        upsertRound(season, round, lockTime)
    }
}
