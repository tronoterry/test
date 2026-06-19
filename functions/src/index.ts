import { initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";
import { onCall, HttpsError } from "firebase-functions/v2/https";

initializeApp();
const db = getFirestore();

interface FinalizeRoundRequest {
  season: number;
  round: number;
}

/**
 * Authoritative, server-side equivalent of TipRepository.finalizeRound() in the Android
 * app. Deploying this function and pointing the Admin screen at it (instead of writing
 * roundResults/seasonStandings directly from the client) removes the need to trust the
 * admin's device for scoring math, at the cost of requiring `firebase deploy --only functions`.
 * Both implementations use the same Firestore document shapes, so either can be used alone.
 */
export const finalizeRound = onCall<FinalizeRoundRequest>(async (request) => {
  const uid = request.auth?.uid;
  if (!uid) {
    throw new HttpsError("unauthenticated", "Sign in required");
  }
  const callerDoc = await db.collection("users").doc(uid).get();
  if (!callerDoc.exists || callerDoc.data()?.isAdmin !== true) {
    throw new HttpsError("permission-denied", "Admin only");
  }

  const { season, round } = request.data;
  if (typeof season !== "number" || typeof round !== "number") {
    throw new HttpsError("invalid-argument", "season and round are required numbers");
  }

  const gamesSnap = await db
    .collection("games")
    .where("season", "==", season)
    .where("round", "==", round)
    .get();
  const games = gamesSnap.docs.map((d) => ({ id: d.id, ...d.data() }));

  if (games.some((g: any) => g.homeScore == null || g.awayScore == null)) {
    throw new HttpsError("failed-precondition", "All games need a final score before finalizing");
  }

  const winners = new Map<string, "HOME" | "AWAY" | "DRAW">();
  for (const g of games as any[]) {
    winners.set(g.id, g.homeScore > g.awayScore ? "HOME" : g.awayScore > g.homeScore ? "AWAY" : "DRAW");
  }

  const tipsSnap = await db
    .collection("tips")
    .where("season", "==", season)
    .where("round", "==", round)
    .get();

  for (const tipDoc of tipsSnap.docs) {
    const tip = tipDoc.data() as { uid: string; picks?: Record<string, string> };
    const picks = tip.picks ?? {};
    let correct = 0;
    for (const [gameId, pick] of Object.entries(picks)) {
      if (winners.get(gameId) === pick) correct++;
    }

    const userDoc = await db.collection("users").doc(tip.uid).get();
    const displayName = (userDoc.data()?.displayName as string) ?? tip.uid;

    const standingRef = db.collection("seasonStandings").doc(`${season}_${tip.uid}`);

    await db.runTransaction(async (txn) => {
      // Firestore transactions require all reads before any writes.
      const existing = await txn.get(standingRef);
      const priorTotal = (existing.data()?.totalCorrect as number) ?? 0;
      const priorRounds = (existing.data()?.roundsScored as number) ?? 0;

      txn.update(tipDoc.ref, { correctCount: correct, scored: true });

      txn.set(db.collection("roundResults").doc(`${season}_${round}_${tip.uid}`), {
        uid: tip.uid,
        displayName,
        season,
        round,
        correct,
        totalGames: games.length,
      });

      txn.set(standingRef, {
        uid: tip.uid,
        displayName,
        season,
        totalCorrect: priorTotal + correct,
        roundsScored: priorRounds + 1,
      });
    });
  }

  await db.collection("rounds").doc(`${season}_${round}`).update({ finalized: true });

  return { scoredTips: tipsSnap.size };
});
