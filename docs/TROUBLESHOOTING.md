# Troubleshooting

## ADB cannot see the TV

Enable developer options and ADB debugging on the TV, accept its authorization prompt, and confirm the address and port. Use `adb devices -l` and pass the exact serial to the installer.

## The screensaver does not start automatically

Select Rain on Glass in the device's screensaver settings and confirm that the device has DreamService support enabled. Android and TV vendors control idle timing and automatic startup.

## The installer rejects a release checksum

Do not install that file. Download the APK and checksum again from the official [releases page](https://github.com/jeremykenedy/rain-on-glass/releases). The installer exits before invoking package installation when the checksum does not match.

## Settings appear unchanged

Settings are read when the preview or screensaver scene starts. Close and reopen the preview after changing an option.
