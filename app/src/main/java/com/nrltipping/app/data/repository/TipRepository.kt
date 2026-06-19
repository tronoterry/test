package com.nrltipping.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.nrltipping.app.data.model.Pick
import com.nrltipping.app.data.model.RoundResult
import com.nrltipping.app.data.model.SeasonStanding
import com.nrltipping.app.data.model.Tip
import kotlinx.coroutines.tasks.await

class TipRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    private fun tipDocId(season: Int, round: Int, uid: String) = "${season}_${round}_${uid}"

    suspend fun getTip(season: Int, round: Int, uid: String): Tip? {
        val snap = firestore.collection("tips").document(tipDocId(season, round, uid)).get().await()
        return snap.toObject(Tip::class.java)
    }

    /**
     * Creates or updates the caller's picks for a round. Firestore security rules
     * independently reject this write once `now >= round.lockTime`, so the 1-hour
     * cutoff is enforced even if a client's local clock is wrong.
     */
    suspend fun submitTip(season: Int, round: Int, uid: String, picks: Map<String, Pick>) {
        val data = mapOf(
            "uid" to uid,
            "season" to season,
            "round" to round,
            "picks" to picks.mapValues { it.value.name },
            "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
        )
        firestore.collection("tips").document(tipDocId(season, round, uid))
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun getWeeklyLeaderboard(season: Int, round: Int): List<RoundResult> {
        val snap = firestore.collection("roundResults")
            .whereEqualTo("season", season)
            .whereEqualTo("round", round)
            .orderBy("correct", Query.Direction.DESCENDING)
            .get()
            .await()
        return snap.documents.mapNotNull { it.toObject(RoundResult::class.java) }
    }

    suspend fun getSeasonLeaderboard(season: Int): List<SeasonStanding> {
        val snap = firestore.collection("seasonStandings")
            .whereEqualTo("season", season)
            .orderBy("totalCorrect", Query.Direction.DESCENDING)
            .get()
            .await()
        return snap.documents.mapNotNull { it.toObject(SeasonStanding::class.java) }
    }

    /**
     * Finalizes scoring for every tip in a round: compares each user's picks against
     * final game results, writes per-tip correct counts, the weekly leaderboard rows,
     * and the season-to-date totals. Run from the admin screen once all of a round's
     * games are final. This mirrors the Cloud Function in functions/src/index.ts so the
     * admin screen still works if Cloud Functions deployment is skipped.
     */
    suspend fun finalizeRound(season: Int, round: Int, games: List<com.nrltipping.app.data.model.Game>) {
        val winners = games.associate { it.id to it.winner }
        val totalGames = games.size

        val tipsSnap = firestore.collection("tips")
            .whereEqualTo("season", season)
            .whereEqualTo("round", round)
            .get()
            .await()
        val tips = tipsSnap.documents.mapNotNull { it.toObject(Tip::class.java) }

        for (tip in tips) {
            val correct = tip.picks.count { (gameId, pickName) ->
                val winner = winners[gameId] ?: return@count false
                winner.name == pickName
            }

            val profileSnap = firestore.collection("users").document(tip.uid).get().await()
            val displayName = profileSnap.getString("displayName") ?: tip.uid

            val standingRef = firestore.collection("seasonStandings").document("${season}_${tip.uid}")

            firestore.runTransaction { txn ->
                // Firestore transactions require all reads before any writes.
                val existing = txn.get(standingRef)
                val priorTotal = existing.getLong("totalCorrect")?.toInt() ?: 0
                val priorRounds = existing.getLong("roundsScored")?.toInt() ?: 0

                val tipRef = firestore.collection("tips").document(tipDocId(season, round, tip.uid))
                txn.update(tipRef, "correctCount", correct, "scored", true)

                val resultRef = firestore.collection("roundResults").document(tipDocId(season, round, tip.uid))
                txn.set(
                    resultRef,
                    RoundResult(
                        uid = tip.uid,
                        displayName = displayName,
                        season = season,
                        round = round,
                        correct = correct,
                        totalGames = totalGames,
                    ),
                )

                txn.set(
                    standingRef,
                    SeasonStanding(
                        uid = tip.uid,
                        displayName = displayName,
                        season = season,
                        totalCorrect = priorTotal + correct,
                        roundsScored = priorRounds + 1,
                    ),
                )
            }.await()
        }

        firestore.collection("rounds").document("${season}_${round}")
            .update("finalized", true)
            .await()
    }
}
