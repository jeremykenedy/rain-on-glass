# Configuration

Open Rain on Glass from the Android TV app list or device screensaver settings. Select Preview animation to see the current effect. Settings are stored locally and take effect when the next preview or dream starts.

| Key | Type | Values | Default |
|---|---|---|---|
| `background` | choice | `night`, `overcast`, `random` | `night` |
| `density` | choice | `few`, `handful`, `many`, `heavy`, `downpour`, `random` | `handful` |
| `motion` | choice | `slow`, `steady`, `fast`, `random` | `steady` |
| `city_glow` | choice | `off`, `on`, `random` | `on` |
| `focus` | choice | `soft`, `crisp`, `random` | `soft` |
| `randomize_all` | boolean | `true`, `false` | `false` |

Each choice can be set to `random` independently. When `randomize_all` is true, every choice is resolved randomly each time a new scene starts.

## Host application interface

The provider authority is `com.jeremykenedy.rainonglass.settings`.

- `content://com.jeremykenedy.rainonglass.settings/schema` returns columns `key`, `title`, `type`, `default`, `choices`, and `randomAllowed`.
- `content://com.jeremykenedy.rainonglass.settings/settings` returns current `key` and `value` rows. Update a row by calling `ContentResolver.update` with a `ContentValues` object containing `key` and `value`.

Only values in the schema are accepted. The master boolean accepts the literal strings `true` and `false`. A client should show the schema choices directly rather than infer its own controls.
