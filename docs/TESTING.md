# Testing

Run the checks from the project root:

```bash
bash test.sh
bash scripts/test-coverage.sh
bash scripts/test-python-coverage.sh
bash scripts/check-style.sh
python3 scripts/check-docs.py
python3 scripts/check-privacy.py
bash build.sh
```

Host tests cover each supported and rejected setting, all value mappings, random option boundaries, and validation failures. JaCoCo enforces 100% line and branch coverage for `RainOptions` and `SettingsValues`. Python coverage enforces 100% line and branch coverage for installer logic. Android framework rendering and DreamService lifecycle are exercised on emulator-5570 and cannot be measured by host JVM coverage.

For a native emulator smoke test, install the APK with ADB, open SettingsActivity, use Preview animation, change settings, and inspect both the preview and the system DreamService selection flow. The evidence and limitations are in [verification notes](VERIFICATION.md).
