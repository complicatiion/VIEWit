# VIEWit Documentation

VIEWit is a WebView-based Android hub for configured web apps. It supports mobile devices and Android TV devices through separate layout strategies.

## Main Areas

- HomeScreen
- BrowserScreen
- SettingsScreen
- Add/Edit Web App
- Adblock Settings
- Privacy DNS
- TV Mode Settings
- About

## Weblibrary

Default entries:

1. YouTube: `https://m.youtube.com`
2. YouTube Music: `https://music.youtube.com`
3. SoundCloud: `https://soundcloud.com`
4. Spotify: `https://open.spotify.com`

Custom entries can define:

- Name
- URL
- Category
- Icon style
- Icon color
- Custom User-Agent
- JavaScript
- Desktop mode
- Adblock
- Cookies
- DOM storage
- Cache
- Favorite

## Browser

The browser uses Android WebView with safe defaults:

- JavaScript per web app
- DOM storage per web app
- Cookies per web app
- Third-party cookies controlled globally
- User-Agent override for desktop mode
- SSL errors are cancelled and not ignored
- No JavaScript bridge is added
- WebView debugging is disabled

## Import and Export

V1.1.2 uses JSON text export to clipboard and paste-based import. This avoids extra storage permissions.

## Notification Player

V1.1.2 adds an optional Notification Player. When enabled, VIEWit can show Android notification controls for the currently opened WebView. The notification shows the current web app name and page title when available. Play, pause, previous, next and stop actions are sent to the active WebView as best-effort JavaScript media commands. Some services may ignore these commands or map them differently.
