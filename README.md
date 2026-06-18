# Cover Letter Generator

A small Flutter Android app that reads your resume (PDF or DOCX), takes a job
description (pasted or uploaded as a file), and asks Claude (Anthropic API)
to write a tailored cover letter using only facts from your resume.

## Features

- Upload a resume as **PDF** or **DOCX** — text is extracted on-device.
- Paste a job description, or upload it as a **PDF / DOCX / TXT** file.
- Generates a cover letter via the Anthropic Messages API using your own API key.
- Copy or share the result.

## Getting an APK

This sandbox can't compile a `.apk` directly (no Android SDK / network access
to `dl.google.com`), so the build runs in GitHub Actions instead:

1. Push to any branch (already wired up via `.github/workflows/build-apk.yml`).
2. Open the repo's **Actions** tab → the latest "Build APK" run → **Artifacts**.
3. Download `cover-letter-app-apk.zip`, which contains:
   - `app-release.apk` — smaller, optimized build (signed with the Flutter debug key, so it installs fine but isn't suitable for the Play Store as-is).
   - `app-debug.apk` — unoptimized, for troubleshooting.
4. Transfer the APK to your phone and tap it to install (you'll need to allow
   "install unknown apps" for whichever app you used to open the file).

To build locally instead (requires Flutter + Android SDK installed):

```bash
flutter pub get
flutter build apk --release
# -> build/app/outputs/flutter-apk/app-release.apk
```

## Setting up the API key

1. Get an API key from https://console.anthropic.com.
2. Open the app → tap the gear icon → paste the key → Save.
3. The key is stored only on-device (Android Keystore, via
   `flutter_secure_storage`) and is sent only to `api.anthropic.com`.

## Project layout

```
lib/
  main.dart
  screens/        # Home, Settings, Result screens
  services/
    file_text_service.dart      # PDF/DOCX -> plain text
    cover_letter_service.dart   # Anthropic API call
    secure_storage_service.dart # API key / model persistence
android/          # standard Flutter Android project
.github/workflows/build-apk.yml
```

## Notes on dependencies

- PDF text extraction uses `qnox_pdf_text` (MIT, wraps PdfBox-Android /
  PDFKit) instead of Syncfusion's PDF package, to avoid requiring a
  Syncfusion license key for what is otherwise a simple personal app.
- DOCX text extraction is hand-rolled (unzip with `archive` + a small regex
  over `word/document.xml`) rather than pulling in a package, since the only
  actively maintained DOCX-to-text package available pinned an old `xml`
  package version that conflicted with other dependencies.
