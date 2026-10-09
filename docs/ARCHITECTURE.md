# Architecture

Rain on Glass is a standalone Android application implemented with the Android SDK and Java. `RainDreamService` owns the Android DreamService lifecycle. `RainSceneView` draws the animated scene with Canvas primitives: a gradient window view, soft background light, fine surface droplets, and moving runoff beads. It uses no bundled images, videos, fonts, or sounds.

`RainOptions` resolves the saved or randomized settings at view creation. `SettingsActivity` provides the remote-friendly settings screen. `SettingsProvider` exposes a versioned schema and current values to host applications. `SettingsValues` validates updates before they are saved to local preferences.

The app has no Internet permission, network SDK, analytics, telemetry, crash reporting, or background services. The standalone Python installer contacts GitHub only when the user explicitly installs or updates the APK.

The canvas redraws at roughly 30 frames per second only while the preview or dream is active. The view stops scheduled draws when the dream pauses or detaches. See [testing](TESTING.md) and [device verification](VERIFICATION.md) for what has been measured and what remains unverified.
