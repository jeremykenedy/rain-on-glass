<p align="center">
  <a href="https://github.com/jeremykenedy/rain-on-glass#gh-light-mode-only"><picture><source media="(max-width: 600px) and (prefers-reduced-motion: reduce)" srcset="art/banner-mobile-still-light.1be76b09f85b.svg"><source media="(prefers-reduced-motion: reduce)" srcset="art/banner-still-light.16fce21c09a8.svg"><source media="(max-width: 600px)" srcset="art/banner-mobile-light.9954dd50e67e.svg"><img src="art/banner-light.0c35cbeb58cb.svg#gh-light-mode-only" alt="Rain on Glass" width="800"></picture></a>
  <a href="https://github.com/jeremykenedy/rain-on-glass#gh-dark-mode-only"><picture><source media="(max-width: 600px) and (prefers-reduced-motion: reduce)" srcset="art/banner-mobile-still-dark.9030830dddc1.svg"><source media="(prefers-reduced-motion: reduce)" srcset="art/banner-still-dark.ab6a6f39e971.svg"><source media="(max-width: 600px)" srcset="art/banner-mobile-dark.975aebd236ac.svg"><img src="art/banner-dark.610c594e8681.svg#gh-dark-mode-only" alt="Rain on Glass" width="800"></picture></a>
</p>

<p align="center">An animated rain-on-glass screensaver for Fire TV, Android TV, and Google TV. No ads, analytics, or tracking.</p>

<p align="center">
  <a href="https://github.com/jeremykenedy/rain-on-glass/releases"><img src="https://img.shields.io/github/downloads/jeremykenedy/rain-on-glass/total?label=downloads" alt="GitHub release downloads"></a>
  <a href="https://github.com/jeremykenedy/rain-on-glass/releases/latest"><img src="https://img.shields.io/github/v/release/jeremykenedy/rain-on-glass?label=release" alt="Latest release"></a>
  <a href="https://github.com/jeremykenedy/rain-on-glass/actions/workflows/ci.yml"><img src="https://github.com/jeremykenedy/rain-on-glass/actions/workflows/ci.yml/badge.svg?branch=main" alt="Build, tests, and coverage"></a>
  <a href="https://github.com/jeremykenedy/rain-on-glass/actions/workflows/style.yml"><img src="https://github.com/jeremykenedy/rain-on-glass/actions/workflows/style.yml/badge.svg?branch=main" alt="Code style"></a>
  <a href="https://github.com/jeremykenedy/rain-on-glass/actions/workflows/docs.yml"><img src="https://github.com/jeremykenedy/rain-on-glass/actions/workflows/docs.yml/badge.svg?branch=main" alt="Documentation checks"></a>
  <a href="https://github.com/jeremykenedy/rain-on-glass/actions/workflows/security.yml"><img src="https://github.com/jeremykenedy/rain-on-glass/actions/workflows/security.yml/badge.svg?branch=main" alt="Privacy checks"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache--2.0-blue.svg" alt="Apache License 2.0"></a>
</p>

<p align="center">
  <a href="https://github.com/jeremykenedy"><img src="https://img.shields.io/badge/Follow-jeremykenedy-181717?logo=github" alt="Follow jeremykenedy on GitHub"></a>
  <a href="https://github.com/jeremykenedy/rain-on-glass"><img src="https://img.shields.io/badge/Star-this%20repo-ffcb47" alt="Star this repository"></a>
  <a href="https://github.com/jeremykenedy/rain-on-glass/stargazers"><img src="https://img.shields.io/github/stars/jeremykenedy/rain-on-glass" alt="GitHub stars"></a>
  <a href="https://github.com/sponsors/jeremykenedy"><img src="https://img.shields.io/badge/Sponsor-jeremykenedy-ea4aaa" alt="Sponsor jeremykenedy"></a>
</p>

Show some love by starring this repository on GitHub.

## Table of contents

- [Privacy](#privacy)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Screenshots](#screenshots)
- [Building and testing](#building-and-testing)
- [Documentation](#documentation)
- [License](#license)

## Privacy

The app requests no network permission and has no ads, analytics, telemetry, crash reporting, tracking, or runtime network requests. Settings stay on the device. The optional installer contacts GitHub only when you explicitly run install or update to download the published APK and checksum. It sends no device or usage information.

## Features

- Continuously animated rain beads and runoff trails over a softly lit, defocused background.
- Night or overcast background, five rain densities, three speeds, distant light, and soft or crisp glass focus.
- Each setting can be randomized independently, or all supported settings can be randomized when the screensaver starts.
- Remote-friendly settings and preview screens.
- Settings schema and values exposed through an app-owned content provider for host applications.
- GPU-rendered refraction, highlights and runoff over original bundled city backgrounds. No runtime downloads.

## Requirements

- Fire TV, Android TV, or Google TV with Android 6.0 (API 23) or newer and DreamService support and OpenGL ES 2.0.
- A computer with Python 3 and Android Debug Bridge (ADB) for CLI installation.
- Android SDK platform 36 and build-tools 36.0.0 to build from source.

The 1.1.0 candidate has been checked on a Google TV API 34 emulator at 1920x1080, including an actual bound DreamService. Physical Fire TV, Android TV and Google TV acceptance remains pending. See [verification notes](docs/VERIFICATION.md).

## Installation

Connect the TV to ADB, then run the guided installer. It downloads the latest stable APK and checksum from this repository, verifies SHA-256, and installs or updates the app:

```bash
python3 install.py --serial TV_IP:5555
```

For scripts, add `--yes`. To install a locally built APK, add `--apk build/rain-on-glass.apk`. To remove the app, run `python3 install.py --serial TV_IP:5555 --uninstall` and confirm. Non-interactive uninstall requires `--uninstall --yes --force`.

After installation, choose Rain on Glass in the device's screensaver settings. The installer does not change the selected screensaver, system timers, or device update settings. See [installation](docs/INSTALLATION.md) for details.

## Configuration

Open Rain on Glass settings from the app list or the device's screensaver settings. Changes apply the next time the dream starts. Each choice includes Random; the master toggle randomizes all options at startup.

| Setting | Choices | Default |
|---|---|---|
| Window lighting | Night, Overcast, Random | Night |
| Rain density | A few, A handful, Many, Heavy, Downpour, Random | A handful |
| Rain speed | Slow, Steady, Fast, Random | Steady |
| Distant light | Off, On, Random | On |
| Glass focus | Soft, Crisp, Random | Soft |
| Randomize all | On, Off | Off |

Host applications can query `content://com.jeremykenedy.rainonglass.settings/schema` and `/settings`, then update an individual setting using the `key` and `value` columns at `/settings`. See [configuration](docs/CONFIGURATION.md).

## Screenshots

<table>
  <tr>
    <td><img src="art/screenshots/rain-on-glass-night.png" alt="Animated night rain on glass scene" width="800"></td>
    <td><img src="art/screenshots/rain-on-glass-overcast.png" alt="Animated overcast rain on glass scene" width="800"></td>
  </tr>
  <tr><td colspan="2"><img src="art/screenshots/rain-on-glass-settings.png" alt="Rain on Glass settings on the Android TV emulator" width="800"></td></tr>
</table>

The current candidate captures are from emulator-5586, Google TV API 34, after the native animation started. It contains no app chrome, words, or watermark.

## Building and testing

```bash
bash build.sh
bash test.sh
bash scripts/test-coverage.sh
bash scripts/test-python-coverage.sh
bash scripts/check-style.sh
python3 scripts/check-docs.py
python3 scripts/check-privacy.py
```

Owned option resolution and settings validation have 100% line and branch coverage. Android framework lifecycle, graphics APIs, and system screensaver selection are validated separately on the emulator. Read [building](docs/BUILDING.md), [testing](docs/TESTING.md), and [CI](docs/CI.md).

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [Building](docs/BUILDING.md)
- [CI](docs/CI.md)
- [Configuration](docs/CONFIGURATION.md)
- [Installation, update, and removal](docs/INSTALLATION.md)
- [Release process](docs/RELEASING.md)
- [Testing](docs/TESTING.md)
- [Troubleshooting](docs/TROUBLESHOOTING.md)
- [Device verification](docs/VERIFICATION.md)
- [v1.1.0 candidate notes](docs/releases/v1.1.0.md)
- [v1.0.0 release notes](docs/releases/v1.0.0.md)

## License

Rain on Glass is licensed under the [Apache License 2.0](LICENSE). See [NOTICE](NOTICE) for project notices.
