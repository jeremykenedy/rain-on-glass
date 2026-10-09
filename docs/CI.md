# Continuous integration

GitHub Actions runs the Java option and provider validation tests, installer tests, 100% line and branch coverage gates for owned testable logic, Android compilation, APK permission inspection, Python compilation, documentation link checks, and privacy checks. The workflows use pinned GitHub Actions revisions.

CI builds an unsigned review APK and checks its package, version, DreamService permission, settings provider, privacy manifest, and checksum. The release APK is signed locally. CI creates no production signature and does not need signing secrets. Token-gated or paid quality services are not configured. No quality ratings are claimed for providers that are not running.
