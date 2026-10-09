# Device verification

## Android TV emulator

- Device: `emulator-5570`, Android TV 64-bit emulator image, Android 12/API 31, 1920x1080.
- App: `com.jeremykenedy.rainonglass`.
- Verified: signed APK installs, package metadata resolves the DreamService, settings activity launches, preview renders the animated scene, provider schema is readable, and the settings screen is visible at 1920x1080.
- Screenshots were captured from the running preview and settings activity after launch. See `docs/screenshots/`.

## Limits

The emulator image has no system Dream settings activity, so system selection and idle activation could not be exercised. No physical Fire TV or Google TV device test has been completed for Rain on Glass. Emulator validation does not establish vendor idle startup, native 4K rendering, hardware efficiency, long-duration thermal performance, deep sleep behavior, or Amazon update persistence. The renderer uses the Android Canvas at the device's logical view size and does not force a 4K buffer.
