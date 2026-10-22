# Device verification

## 1.1.0 candidate

The signed candidate was installed on emulator-5586, Google TV API 34, using the host GPU at a 1920x1080 logical output.

- Native preview was inspected with the night and overcast environments. Both show moving refractive beads and narrow runoff trails over a blurred city.
- A 10.95-second native night recording contains 249 encoded frames, about 22.7 captured frames per second under concurrent emulator and render load. This capture rate does not establish presented frame timing or physical Fire TV performance.
- The real `RainDreamService` was bound through DreamManager in preview mode, stayed rendered, and exited with Back. The emulator's original Backdrop component and enabled setting were restored.
- Settings display the six existing controls. Remote navigation changed density from A handful to Many, and the provider returned the persisted value. High-density and physical motion checks remain pending.
- Current application screenshots are in `art/screenshots/`. Original background dimensions are 1672x941, documented in `assets/background-sources.json`.

## Offline credits follow-up

The current follow-up adds a remote-accessible Credits and licenses button with the project NOTICE and full Apache license bundled in the APK. It preserves all six preference keys and the accepted scene. The signed credits candidate is `ca700f7717ed7d5e5aae1aa26da0615805a55cd8e0e8c066db81e69941aaf730`. All 13 original non-signature, non-DEX APK entries and 11 compiled classes outside SettingsActivity match the preceding 860112 renderer baseline. This is rendering equivalence evidence, not a whole-APK identity claim. Host tests, actual Java formatting, line/branch coverage, installer, privacy and documentation checks pass. The exact native credits and navigation follow-up is recorded below. Physical verification is recorded below; sustained thermal measurements remain untested.

## October 22 physical Fire TV acceptance

The final original-key signed 1.1.0/code 2 APK above was installed in place on a Fire TV AFTDEC012E/API 30. All six exposed pre-update preferences survived. The night/downpour/fast/crisp/distant-light preset ran for 40 seconds before a SurfaceFlinger sample measured 30.2417 actual presents per second across 126 presents (median 33.325 ms, maximum 33.575 ms). This is a short production-frame sample, not encoded movie FPS, a thermal endurance test or a native 4K claim.

D-pad Settings reached Preview. Menu left Preview active; no custom Menu action is implemented. Back returned Settings and Home exited the app. A second overcast/few/slow/soft/no-glow preset rendered in the actual selected non-test RainDreamService and exited with Back. An eight-second physical recording was reviewed at normal speed: coherent moving refraction and runoff, no incidental text or watermark.

All six app preferences, the previously selected screensaver, eight protected system settings, the Aquarium and Jellyfish settings, UI backup and foreground protection service were preserved. The original actual Aquarium Dream was restored. No timer was changed and no app data was cleared or uninstalled.

Ordinary automatic idle entry, reboot behavior, sustained thermal measurements, physical Android TV/Google TV models and native 4K buffers remain untested. Physical screenshots and sanitized artifact provenance appear in the [current gallery](SCREENSHOTS.md).

## October 22 exact credits candidate

Android API 34 emulator checks used the frozen APK hash above with actual 1920x1080 captures. D-pad navigation reached Credits and licenses, scrolled through the full NOTICE and license to its end, and selected the positive Close button. Preview returned to Settings with Back; the actual DreamService bound and exited with Back. All eight saved system keys and the original screensaver selection were unchanged, and the emulator returned Home. No app data was cleared or uninstalled.

This app was not installed on the shared emulator before the test. Its 6 exposed values were captured after installation and restored exactly; this does not prove pre-upgrade data retention. These are native credits/lifecycle checks, not physical smoothness, automatic idle, 4K or thermal verification.

[Sanitized capture provenance](../art/screenshots/credits-api34.json) records the artifact and screenshot hashes.

<table>
  <tr>
    <td><a href="../art/screenshots/credits-api34-credits.png"><img src="../art/screenshots/credits-api34-credits.png" alt="Full offline credits and notices" width="320"></a></td>
    <td><a href="../art/screenshots/credits-api34-license-end.png"><img src="../art/screenshots/credits-api34-license-end.png" alt="End of the bundled Apache license" width="320"></a></td>
    <td><a href="../art/screenshots/credits-api34-settings.png"><img src="../art/screenshots/credits-api34-settings.png" alt="Current native settings" width="320"></a></td>
  </tr>
</table>
