# Device verification

## Android TV emulator

- Device: `emulator-5570`, Android TV 64-bit emulator image, Android 12/API 31, 1920x1080.
- App: `com.jeremykenedy.rainonglass`.
- Verified: signed APK installs, package metadata resolves the DreamService, settings activity launches, preview renders the animated scene, provider schema is readable, and the settings screen is visible at 1920x1080.
- Screenshots were captured from the running preview and settings activity after launch. See `docs/screenshots/`.

## Limits

The emulator image has no system Dream settings activity, so system selection and idle activation could not be exercised. Emulator validation does not establish vendor idle startup, native 4K rendering, hardware efficiency, long-duration thermal performance, deep sleep behavior, or Amazon update persistence. The renderer uses the Android Canvas at the device's logical view size and does not force a 4K buffer.

| Platform | Device | Result |
| --- | --- | --- |
| Physical Fire TV | Not tested for this release | We are looking for a Fire TV owner to test installation, screensaver selection and activation, and remote settings, then report the model, Fire OS/API, resolution, and results in the [issue tracker](https://github.com/jeremykenedy/rain-on-glass/issues). |
| Physical Android TV | Not tested for this release | We are looking for an Android TV owner to run the same checks and report the model, OS/API, resolution, and results in the [issue tracker](https://github.com/jeremykenedy/rain-on-glass/issues). |
| Physical Google TV | Not tested for this release | We are looking for a Google TV owner to run the same checks and report the model, OS/API, resolution, and results in the [issue tracker](https://github.com/jeremykenedy/rain-on-glass/issues). |
