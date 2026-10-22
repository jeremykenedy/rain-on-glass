# Device verification

## 1.1.0 candidate

The signed candidate was installed on emulator-5586, Google TV API 34, using the host GPU at a 1920x1080 logical output.

- Native preview was inspected with the night and overcast environments. Both show moving refractive beads and narrow runoff trails over a blurred city.
- A 10.95-second native night recording contains 249 encoded frames, about 22.7 captured frames per second under concurrent emulator and render load. This capture rate does not establish presented frame timing or physical Fire TV performance.
- The real `RainDreamService` was bound through DreamManager in preview mode, stayed rendered, and exited with Back. The emulator's original Backdrop component and enabled setting were restored.
- Settings display the six existing controls. Remote navigation changed density from A handful to Many, and the provider returned the persisted value. High-density and physical motion checks remain pending.
- Current application screenshots are in `art/screenshots/`. Original background dimensions are 1672x941, documented in `assets/background-sources.json`.

## Offline credits follow-up

The current follow-up adds a remote-accessible Credits and licenses button with the project NOTICE and full Apache license bundled in the APK. It preserves all six preference keys and the accepted scene. The signed credits candidate is `ca700f7717ed7d5e5aae1aa26da0615805a55cd8e0e8c066db81e69941aaf730`. All original non-DEX APK entries and compiled classes outside SettingsActivity match the preceding 860112 renderer baseline. This is rendering equivalence evidence, not a whole-APK identity claim. Host tests, actual Java formatting, line/branch coverage, installer, privacy and documentation checks pass. Native credits scrolling, exact final Preview/Dream/Back routes and physical quality/performance remain pending.

## Remaining acceptance checks

Physical Fire TV testing, sustained high-density resource measurements, ordinary idle activation, reboot behavior and physical Android TV or Google TV checks remain open. The current candidate uses the surface's logical size and does not force or claim a native 4K buffer. It is not yet a published release.

The earlier 1.0.0 Canvas renderer and its screenshots do not describe this candidate.

| Physical platform | Candidate status | Requested evidence |
| --- | --- | --- |
| Fire TV | Pending | Model, Fire OS/API, resolution, selection, idle activation, remote settings and sustained performance |
| Android TV | Pending | Model, OS/API, resolution, selection, idle activation and remote settings |
| Google TV | Pending | Model, OS/API, resolution, selection, idle activation and remote settings |

Device reports can be submitted through the [issue tracker](https://github.com/jeremykenedy/rain-on-glass/issues).
