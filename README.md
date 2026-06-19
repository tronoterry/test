# NRL Tipping

Android app (Kotlin + Jetpack Compose) for a private NRL tipping competition:
sign up/log in, pick winners against listed odds for the current round, and
track weekly + season-to-date leaderboards. Tips lock 1 hour before the
round's first kickoff.

## Architecture

- **Client**: Kotlin, Jetpack Compose, Navigation Compose, MVVM.
- **Backend**: Firebase Auth (email/password) + Cloud Firestore. No custom
  server is required.
- **Data model** (`app/src/main/java/com/nrltipping/app/data/model/Models.kt`):
  - `users/{uid}` — profile, `isAdmin` flag.
  - `rounds/{season}_{round}` — `lockTime` = kickoff of the round's earliest
    game minus 1 hour, recalculated automatically whenever a fixture is
    added/edited.
  - `games/{gameId}` — fixture, odds, scores once final.
  - `tips/{season}_{round}_{uid}` — one user's picks for a round.
  - `roundResults/{season}_{round}_{uid}` — denormalized weekly leaderboard row.
  - `seasonStandings/{season}_{uid}` — denormalized season-to-date totals.
- **Lock enforcement**: enforced twice — the UI disables editing once
  `lockTime` passes, and `firestore.rules` independently rejects any tip
  write where `request.time >= rounds/{season}_{round}.lockTime`, so a wrong
  device clock can't bypass the cutoff.
- **Fixtures & odds**: there's no public NRL odds API wired up. An admin
  (`isAdmin: true` on their user doc) enters fixtures, odds and final scores
  from the in-app Admin screen. Swap `RoundRepository`/`AdminViewModel` for a
  real feed later — nothing else in the app depends on how that data arrives.
- **Scoring**: the Admin screen's "Finalize round" button computes each
  user's correct picks and updates both leaderboards
  (`TipRepository.finalizeRound`). `functions/src/index.ts` contains an
  equivalent Cloud Function callable (`finalizeRound`) if you'd rather not
  trust the admin's device to run the math — deploying it is optional.

## One-time setup

1. **Create a Firebase project** at https://console.firebase.google.com.
2. Enable **Authentication → Email/Password**.
3. Enable **Firestore** (production mode).
4. **Register an Android app** in the Firebase project with package name
   `com.nrltipping.app`, download the real `google-services.json`, and
   replace the placeholder at `app/google-services.json`. The placeholder
   in this repo has dummy IDs and will not work as-is.
5. Deploy security rules and indexes (requires the
   [Firebase CLI](https://firebase.google.com/docs/cli)):
   ```
   firebase login
   firebase use --add        # pick your project
   firebase deploy --only firestore:rules,firestore:indexes
   ```
6. **Create the first admin**: sign up normally in the app, then in the
   Firestore console open `users/{your-uid}` and set `isAdmin` to `true`.
   New accounts can never set this on themselves — `firestore.rules` blocks
   it — so this manual step is required at least once.
7. (Optional) Deploy the Cloud Function instead of relying on the client-side
   `finalizeRound()`:
   ```
   cd functions && npm install && npm run deploy
   ```

## Building the APK

This repo was authored in a sandboxed environment with no Android SDK and
no network access to download one, so the APK itself could not be built or
tested here. Two ways to get a real APK:

- **GitHub Actions** (`.github/workflows/android-build.yml`): runs on every
  push, builds a debug APK on a runner that has the Android SDK, and
  uploads it as a workflow artifact named `nrl-tipping-debug-apk`.
- **Locally**, with Android Studio (Ladybug+) or the SDK command-line tools
  installed:
  ```
  ./gradlew assembleDebug      # output: app/build/outputs/apk/debug/app-debug.apk
  ```
  Open the project in Android Studio first if you don't have the SDK
  configured — it will offer to install it.

A release build needs a signing config (`./gradlew assembleRelease`) — not
set up here since it depends on a keystore only you should hold.

## Using the app

- **Sign up / sign in** — email + password.
- **Tips tab** — shows the current open round's fixtures with odds; pick a
  side per game and tap "Save tips" any time before the round locks. Re-open
  the tab and change picks as many times as you like up to that point.
- **Leaderboard tab** — "Weekly" (pick a round chip to see that round's
  results) and "Season" (cumulative totals) tabs.
- **Profile tab** — shows your name/email, sign out, and (for admins) a
  shortcut into the Admin screen.
- **Admin screen** (admins only) — set the round number, add fixtures
  (teams, kickoff date/time, odds), enter final scores per game, then
  "Finalize round" once every game in the round is final to score every
  user's tips and update both leaderboards.
