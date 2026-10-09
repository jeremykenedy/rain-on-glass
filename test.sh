#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
OUT="$ROOT/build/tests"
mkdir -p "$OUT"
javac -source 8 -target 8 -d "$OUT" \
  "$ROOT/src/com/jeremykenedy/rainonglass/RainOptions.java" \
  "$ROOT/src/com/jeremykenedy/rainonglass/SettingsValues.java" \
  "$ROOT/tests/RainOptionsTest.java"
java -ea -cp "$OUT" com.jeremykenedy.rainonglass.RainOptionsTest
python3 -m unittest -v tests.test_installer
