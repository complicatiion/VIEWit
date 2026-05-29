# VIEWit Third-Party Licenses and Notices

This file documents third-party technologies, libraries, platform APIs, services and assets that may be used, referenced or opened by VIEWit.

This notice file is not a replacement for the full license texts of the respective projects. If a dependency, platform component or optional integration includes its own license file, attribution notice or legal terms, those terms remain authoritative and must be kept intact.

---

## Project license relationship

VIEWit itself is licensed under the custom VIEWit non-commercial attribution license in:

```text
LICENSE.md
```

Third-party components remain licensed under their own licenses. The VIEWit license does not relicense third-party code, third-party services, Android platform components, Kotlin components, AndroidX libraries, Google libraries, website content, trademarks or service brands.

---

## Main Android and Kotlin dependencies

The Android project may use the following Gradle dependencies and platform technologies.

| Component | Typical role in VIEWit | License / notice |
|---|---|---|
| Android SDK / Android platform APIs | Android app runtime APIs, WebView integration, notification APIs, intent handling and device features | Android Open Source Project components are generally provided under open-source licenses such as Apache License 2.0, with some device/system components subject to their own terms. |
| Kotlin | Main programming language and compiler/plugin ecosystem | Kotlin is developed by JetBrains and is commonly distributed under Apache License 2.0. |
| Android Gradle Plugin | Android build tooling | Google Android build tooling; used at build time and subject to its own license terms. |
| AndroidX Core KTX | Kotlin extensions and Android compatibility APIs | AndroidX libraries are commonly distributed under Apache License 2.0. |
| AndroidX Activity Compose | Compose integration for Android activities | AndroidX / Apache License 2.0. |
| AndroidX Lifecycle Runtime KTX | Lifecycle-aware runtime utilities | AndroidX / Apache License 2.0. |
| AndroidX Lifecycle ViewModel Compose | ViewModel integration for Compose | AndroidX / Apache License 2.0. |
| Jetpack Compose UI | Declarative UI framework | AndroidX / Apache License 2.0. |
| Jetpack Compose UI Tooling Preview | Compose previews and UI tooling support | AndroidX / Apache License 2.0. |
| Jetpack Compose Material 3 | Material 3 UI components | AndroidX / Apache License 2.0. |
| Jetpack Compose Material Icons Extended | Material icon set used by Compose UI elements | Google / AndroidX component, commonly under Apache License 2.0. |
| Android WebView | Renders external web pages inside the app | Device/system WebView implementation. The app uses platform APIs and does not bundle the WebView engine. |

---

## Android WebView notice

VIEWit uses Android WebView to display websites inside the app.

The actual WebView engine is supplied by the Android device or the installed Android System WebView / browser provider. VIEWit does not bundle Chromium or a standalone browser engine.

Behavior, site compatibility, cookies, media playback, fullscreen playback, login state, DRM behavior, notifications and web rendering can differ between devices and WebView versions.

---

## External web services

VIEWit can open external websites and web apps through Android WebView, including but not limited to:

| Service | Notes |
|---|---|
| YouTube | External Google / YouTube service opened through WebView. |
| YouTube Music | External Google / YouTube Music service opened through WebView. |
| SoundCloud | External SoundCloud service opened through WebView. |
| Spotify | External Spotify service opened through WebView. |
| Custom user-defined web apps | Any URL added by the user is opened as an external website through WebView. |

These services are not part of VIEWit. Their trademarks, logos, websites, content, accounts, APIs, playback systems, terms of service and privacy policies belong to their respective owners.

VIEWit is not affiliated with, sponsored by, endorsed by or officially connected to those external services unless explicitly stated by the respective rights holder.

Users are responsible for complying with the terms and legal requirements of the services they open through VIEWit.

---

## DNSforge notice

VIEWit may display DNSforge DNS values for convenience.

VIEWit does not operate DNSforge, does not provide DNSforge infrastructure and does not automatically configure system DNS settings.

Android Private DNS must be configured manually by the user in Android system settings if the user wants to use DNSforge or another DNS provider system-wide.

DNSforge names, service infrastructure and related rights belong to their respective owner or operator.

---

## Adblocking architecture

VIEWit includes a modular best-effort adblocking architecture.

| Component | Status | Notice |
|---|---|---|
| NoOpAdblockEngine | Included local fallback | Blocks nothing. No third-party adblocking engine is bundled. |
| SimpleHostBlockEngine | Included local implementation | Lightweight host/domain matching. It is not a full ABP-compatible parser. |
| AdblockPlusEngine | Stub only | Prepared for optional future integration. No AdblockPlus binary is bundled by default. |

If a future build integrates AdblockPlus or another adblocking engine, the relevant license terms, notices and distribution requirements of that project must be reviewed and included separately.

---

## Notification APIs

VIEWit may use Android notification APIs and AndroidX notification compatibility helpers for its optional Notification Player.

Notification media controls are best effort. Whether play, pause, next, previous or stop commands work depends on the website, WebView behavior, Android version and playback implementation.

---

## Local assets

VIEWit includes local project assets such as:

- `logo.png`
- app launcher icons
- drawable resources
- vector icons
- local UI graphics and design tokens

Unless explicitly stated otherwise, VIEWit-specific assets are part of the VIEWit project and are covered by the VIEWit project license. Third-party icons, platform icons or generated assets remain subject to their own license terms where applicable.

---

## Material icons

VIEWit may use Material icon components through Jetpack Compose Material Icons Extended.

Material icons are provided by Google through the AndroidX / Compose ecosystem and are generally distributed under Apache License 2.0. Keep the relevant AndroidX and Google notices intact when redistributing builds or source code.

---

## Build-time tools

The project may require build-time tools such as:

- Android Studio
- Gradle or Gradle wrapper
- Android Gradle Plugin
- Kotlin compiler and Kotlin Gradle plugin
- Android SDK command-line tools

These tools are used to build the app and are subject to their own license terms. They are not relicensed by VIEWit.

---

## No bundled CDN assets

VIEWit is intended to avoid external CDN assets for its own app UI. The app interface uses local Android resources and bundled project files.

Websites opened through WebView may load their own external scripts, media, fonts, stylesheets, tracking resources, ads or other third-party resources. Those resources are controlled by the external website, not by VIEWit.

---

## Trademark disclaimer

All product names, service names, company names, logos and trademarks mentioned in VIEWit documentation or UI belong to their respective owners.

Use of those names is for identification and compatibility description only. It does not imply endorsement, sponsorship, partnership or official status.

---

## License summary

| Area | License handling |
|---|---|
| VIEWit app source and assets | Covered by the VIEWit non-commercial attribution license unless otherwise stated. |
| AndroidX / Jetpack / Compose libraries | Remain under their respective AndroidX / Google licenses. |
| Kotlin tooling and libraries | Remain under their respective JetBrains / Kotlin licenses. |
| Android platform / WebView | Supplied by Android/device/system components; not bundled as a standalone browser engine. |
| External web services | Not owned, bundled or relicensed by VIEWit. |
| Optional future adblocking engines | Must include and follow their own licenses if integrated. |
| User-created settings and exports | Belong to the user, unless they include third-party protected material. |

---

## No legal advice

This third-party notice file is provided for project documentation purposes and is not legal advice.

For formal redistribution, commercial use, app-store publication, open-source compliance review or legal approval, consult a qualified legal professional and review the exact dependency graph of the build being distributed.

---


