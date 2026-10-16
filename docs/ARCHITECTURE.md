# Architecture

Rain on Glass is a standalone Android SDK application. `RainDreamService` owns the screensaver lifecycle; `PreviewActivity` uses the same `RainSceneView` renderer.

The OpenGL ES 2.0 renderer draws the distant city and a single batched layer of animated refractive glass beads. Original bundled city images provide the stationary environment. Preblurred variants support soft and crisp glass focus. Droplet geometry is uploaded once to a vertex buffer; vertex shaders animate runoff, and fragment shaders sample the environment through each bead with curved refraction, rim lighting and narrow trails. No textures are generated or downloaded while running.

`RainOptions` resolves saved or randomized settings at view creation. `SettingsActivity` provides remote controls. `SettingsProvider` exposes the schema and values; `SettingsValues` rejects unsupported updates before preferences are saved.

The view requests frames about every 33 milliseconds while visible and pauses its GL surface and frame callbacks when stopped. This is a scheduling target, not a measured hardware performance guarantee. The app has no Internet permission, dependencies, ads, analytics, tracking, network SDKs or background worker services. See [testing](TESTING.md) and [device verification](VERIFICATION.md).
