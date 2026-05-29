# Adblock Architecture

VIEWit uses a modular adblocking interface.

```kotlin
interface AdblockEngine {
    fun isEnabled(): Boolean
    fun shouldBlock(url: String, resourceType: String? = null): Boolean
    fun updateLists()
    fun addUserRule(rule: String)
    fun removeUserRule(rule: String)
}
```

## NoOpAdblockEngine

Safe fallback engine. It blocks nothing and allows the app to run even when other engines are unavailable.

## SimpleHostBlockEngine

A small local engine that checks request hosts and URLs against simple fragments.

Default examples:

- doubleclick.net
- googlesyndication.com
- googleadservices.com
- adservice.google.com
- pagead2.googlesyndication.com
- tracking
- analytics

This engine is not a full ABP parser.

## AdblockPlusEngine

Prepared stub for future `libadblockplus-android` integration. The app must remain buildable without any external AdblockPlus binary.

## WebView Request Filtering

`ViewitWebViewClient.shouldInterceptRequest()` checks each request against the active engine. Blocked requests return an empty `WebResourceResponse`. Normal requests continue through WebView.

## Limitations

Dynamic streaming web apps can still show ads. VIEWit describes all blocking as best effort.
