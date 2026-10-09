# Installation, update, and removal

## Install or update from GitHub

Enable ADB debugging and connect the TV to the computer. Run:

```bash
python3 install.py --serial TV_IP:5555
```

The installer displays its target and asks for confirmation. It downloads the latest stable release metadata from the project's GitHub API, then downloads the release APK and checksum over HTTPS. It verifies the SHA-256 checksum before calling ADB to install. It does not alter the device's selected screensaver, timers, sleep behavior, or system update settings.

For unattended scripts, pass `--yes`. If more than one TV is connected, pass `--serial` to choose the target.

## Install a local build

```bash
bash build.sh
python3 install.py --serial TV_IP:5555 --apk build/rain-on-glass.apk
```

## Uninstall

```bash
python3 install.py --serial TV_IP:5555 --uninstall
```

The installer confirms before removing only `com.jeremykenedy.rainonglass`. For non-interactive removal, both `--yes` and `--force` are required. The installer does not modify other apps or system settings.
