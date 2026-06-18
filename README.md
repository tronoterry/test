# CRF450RL Companion

An unofficial Android app for the 2025 Honda CRF450RL covering maintenance, troubleshooting, and mods, with step-by-step guides and original schematic diagrams (no copyrighted Honda manual images are used).

## Structure

- **Maintenance** — 12 guides (pre-ride inspection, oil/filter, air filter, chain, valve clearance, spark plug, coolant, brakes, suspension sag, tires, battery/electrical, fastener torque)
- **Troubleshooting** — 7 diagnostic flows (won't start, stalling/bogging, overheating, power loss, electrical/lighting, clutch, handling)
- **Mods & Upgrades** — 7 guides (exhaust, ECU tuning, suspension respring, gearing, protection, graphics/seat, bars/ergonomics)
- **Specs Reference** — engine/chassis/service specs
- **About & Disclaimer** — legal/safety disclaimer and diagram sourcing note

Navigation is a Material3 navigation drawer; built with Kotlin + Jetpack Compose.

## Building the APK

This project was built in a sandboxed environment where Google's Maven repository (`dl.google.com`, which hosts the Android Gradle Plugin, Jetpack/Compose libraries, and SDK components) was not reachable, so a compiled `.apk` could not be produced here.

To build it yourself:

1. Open this folder in Android Studio (or run Gradle from a machine with normal internet access and the Android SDK installed).
2. Let Gradle sync — it will pull the Android Gradle Plugin and Compose dependencies from Google's Maven repo and Maven Central.
3. Run `./gradlew assembleDebug` (or use Android Studio's Run button) to produce `app/build/outputs/apk/debug/app-debug.apk`.

No other setup is required — `minSdk` is 24, `targetSdk`/`compileSdk` is 34.

## Disclaimer

This app is an independent, unofficial reference, not affiliated with or endorsed by Honda. Maintenance figures (intervals, torque values, fluid capacities) are general reference points — always confirm against Honda's official owner's/service manual for your specific bike before performing work.
