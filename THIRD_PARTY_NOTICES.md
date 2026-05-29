# Third-Party Notices

VIEWit V1.1.2 uses Android platform APIs and AndroidX libraries through Gradle dependencies.

## AndroidX and Jetpack Compose

The project uses AndroidX Core, Activity Compose, Lifecycle, Jetpack Compose UI, and Material 3. These libraries are distributed by Google under their respective open-source licenses.

## Android WebView

VIEWit uses the Android system WebView component available on the device. Web content is rendered by the system WebView implementation installed on the device.

## DNSforge

DNSforge values are displayed for user convenience. VIEWit does not operate DNSforge and does not automatically configure DNS settings.

## AdblockPlus

No AdblockPlus binary is bundled in V1.1.2. The `AdblockPlusEngine` class is a local stub for future optional integration.

## Logo

The included `logo.png` is part of the VIEWit project assets provided by the project owner.

## Android Notifications

VIEWit uses Android notification APIs and AndroidX Core NotificationCompat for the optional Notification Player controls. Notification media commands are best effort because each WebView service can handle playback controls differently.
