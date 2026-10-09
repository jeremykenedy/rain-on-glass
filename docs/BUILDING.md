# Building

## Requirements

- JDK 17 or newer
- Android SDK platform 36 and build-tools 36.0.0
- `javac`, `keytool`, `openssl`, `zip`, and `shasum`

## Build a signed APK

```bash
bash build.sh
```

The build creates `build/rain-on-glass.apk` and `build/rain-on-glass.apk.sha256`. A project-specific RSA signing key is generated under `~/.android/` the first time it is built. Keep that key and its password file backed up securely. They are not part of the repository. Restoring an existing signing key is required for upgrade-compatible future APKs.

Set `VERSION_NAME`, `VERSION_CODE`, `RAIN_ON_GLASS_KEYSTORE`, and `RAIN_ON_GLASS_KEYPASS` to override defaults. Never put a release key or password into the repository or CI.
