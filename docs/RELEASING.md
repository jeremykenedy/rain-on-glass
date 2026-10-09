# Release process

1. Finish source, tests, docs, screenshots, and repository metadata.
2. Run every local check listed in [testing](TESTING.md).
3. Build the signed release APK with the preserved release key and verify the application ID, version, permissions, and signature.
4. Commit and push the release candidate. Wait for every required GitHub Actions workflow to pass on that exact commit.
5. Make a final read-only review. If anything changes, repeat checks and CI.
6. Create a SemVer tag and attach `rain-on-glass.apk` and `rain-on-glass.apk.sha256` with complete release notes.
7. Download the published assets and verify the checksum. Keep the signing key and password secure for later updates.

The CLI installer obtains the latest stable release on explicit user request; the app itself does not check for updates.
