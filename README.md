# NSW Surf Cams Widget

Android home-screen widget showing NSW surf cam thumbnails in a grid. Tapping
a thumbnail opens a fullscreen live player (Media3/ExoPlayer, HLS-capable).

## Structure

- `SurfCamRepository` — the list of NSW beaches and their `thumbnailUrl` /
  `streamUrl`. This is the single place to wire in real feeds.
- `SurfCamWidgetProvider` / `SurfCamWidgetService` / `SurfCamRemoteViewsFactory`
  — the home-screen widget: a collection (`GridView`) widget so it can hold
  an arbitrary number of cams, each thumbnail carrying its own click intent.
- `FullscreenPlayerActivity` — fullscreen, immersive ExoPlayer screen launched
  with a cam name + stream URL.
- `MainActivity` / `SurfCamAdapter` — a plain in-app grid (same data, same
  fullscreen player) so the app is also usable standalone, not just as a widget.

## Live stream caveat

No NSW surf cam provider (Coastalwatch, Swellnet, Surfline, individual
council/SLSC cams) currently exposes a free, ToS-compliant raw stream URL —
their live feeds are gated behind subscriptions/their own apps, and scraping
or rehosting them would violate their terms of service.

Every cam in `SurfCamRepository` currently points at a public HLS test stream
(`https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8`) so the full click →
fullscreen-playback pipeline works end-to-end. To go live, replace each cam's
`streamUrl` (and `thumbnailUrl`) with a licensed feed once you've arranged
API/embed access with a provider.

## Build & run

Requires Android Studio (or the Android SDK + a connected device/emulator) —
this sandbox has no Android SDK installed, so the build hasn't been verified
here.

1. Open the project root in Android Studio and let it sync.
2. Run the `app` module on a device/emulator (API 26+).
3. Long-press the home screen → Widgets → "NSW Surf Cams" → drag onto the
   home screen.
4. Tap any thumbnail to play that cam fullscreen.
