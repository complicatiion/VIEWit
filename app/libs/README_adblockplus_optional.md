# Optional AdblockPlus Integration

VIEWit V1.1.2 compiles without any external AdblockPlus AAR or module.

The included `AdblockPlusEngine` is a safe stub. It exists so a later version can wire in `libadblockplus-android` without changing the app architecture.

Recommended future integration steps:

1. Add the local AdblockPlus AAR to this directory or add a local Gradle module.
2. Keep the `AdblockEngine` interface unchanged.
3. Replace the stub logic in `AdblockPlusEngine` with the real engine adapter.
4. Keep `SimpleHostBlockEngine` available as a fallback.
5. Keep all adblocking UI copy clear that blocking is best effort.

Do not make the main app depend on a missing external binary. VIEWit must remain buildable without optional AdblockPlus files.
