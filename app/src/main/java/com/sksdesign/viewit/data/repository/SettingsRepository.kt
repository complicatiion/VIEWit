package com.sksdesign.viewit.data.repository

import android.content.Context
import com.sksdesign.viewit.data.model.BrowserControlSettings
import com.sksdesign.viewit.data.model.DeviceMode
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.NotificationPlayerSettings
import com.sksdesign.viewit.data.model.TvModeSettings
import com.sksdesign.viewit.data.storage.JsonStorage
import com.sksdesign.viewit.data.storage.PreferencesStore
import org.json.JSONObject

class SettingsRepository(context: Context) {
    private val store = PreferencesStore(context)

    fun loadTheme(): ThemeSettings = ThemeSettings(
        accentColorHex = store.getString(KEY_ACCENT, "#FFFFFF"),
        glassIntensity = store.getFloat(KEY_GLASS, 0.12f),
        glowIntensity = store.getFloat(KEY_GLOW, 0.80f),
        cardRadius = store.getFloat(KEY_RADIUS, 26f),
        reducedMotion = store.getBoolean(KEY_REDUCED_MOTION, false)
    )

    fun saveTheme(settings: ThemeSettings) {
        store.putString(KEY_ACCENT, settings.accentColorHex)
        store.putFloat(KEY_GLASS, settings.glassIntensity)
        store.putFloat(KEY_GLOW, settings.glowIntensity)
        store.putFloat(KEY_RADIUS, settings.cardRadius)
        store.putBoolean(KEY_REDUCED_MOTION, settings.reducedMotion)
    }

    fun loadTvMode(): TvModeSettings = TvModeSettings(
        deviceMode = DeviceMode.fromStored(store.getString(KEY_DEVICE_MODE, DeviceMode.MOBILE.name)),
        tvLayoutScale = store.getString(KEY_TV_SCALE, "comfortable"),
        showTvControlOverlay = store.getBoolean(KEY_TV_OVERLAY, true),
        autoHideBrowserControls = store.getBoolean(KEY_TV_AUTO_HIDE, true),
        focusGlowIntensity = store.getFloat(KEY_TV_FOCUS_GLOW, 0.65f),
        startInLastWebApp = store.getBoolean(KEY_START_LAST, false),
        remoteBackBehavior = store.getString(KEY_REMOTE_BACK, "webview_back_first"),
        dpadScrollSpeed = store.getFloat(KEY_DPAD_SPEED, 1.0f),
        remoteInputEnabled = store.getBoolean(KEY_REMOTE_INPUT, true),
        enableMediaKeys = store.getBoolean(KEY_MEDIA_KEYS, true),
        showLargeCards = store.getBoolean(KEY_LARGE_CARDS, true),
        preferLandscapeLayout = store.getBoolean(KEY_LANDSCAPE, true)
    )

    fun saveTvMode(settings: TvModeSettings) {
        store.putString(KEY_DEVICE_MODE, settings.deviceMode.name)
        store.putString(KEY_TV_SCALE, settings.tvLayoutScale)
        store.putBoolean(KEY_TV_OVERLAY, settings.showTvControlOverlay)
        store.putBoolean(KEY_TV_AUTO_HIDE, settings.autoHideBrowserControls)
        store.putFloat(KEY_TV_FOCUS_GLOW, settings.focusGlowIntensity)
        store.putBoolean(KEY_START_LAST, settings.startInLastWebApp)
        store.putString(KEY_REMOTE_BACK, settings.remoteBackBehavior)
        store.putFloat(KEY_DPAD_SPEED, settings.dpadScrollSpeed)
        store.putBoolean(KEY_REMOTE_INPUT, settings.remoteInputEnabled)
        store.putBoolean(KEY_MEDIA_KEYS, settings.enableMediaKeys)
        store.putBoolean(KEY_LARGE_CARDS, settings.showLargeCards)
        store.putBoolean(KEY_LANDSCAPE, settings.preferLandscapeLayout)
    }

    fun loadBrowserControls(): BrowserControlSettings = BrowserControlSettings(
        thirdPartyCookies = store.getBoolean(KEY_THIRD_PARTY_COOKIES, false),
        mediaPlaybackWithoutGesture = store.getBoolean(KEY_MEDIA_NO_GESTURE, true),
        cacheMode = store.getString(KEY_CACHE_MODE, "default"),
        defaultDesktopMode = store.getBoolean(KEY_DEFAULT_DESKTOP, false),
        defaultJavaScript = store.getBoolean(KEY_DEFAULT_JS, true),
        autoHideControls = store.getBoolean(KEY_AUTO_HIDE_CONTROLS, true)
    )

    fun saveBrowserControls(settings: BrowserControlSettings) {
        store.putBoolean(KEY_THIRD_PARTY_COOKIES, settings.thirdPartyCookies)
        store.putBoolean(KEY_MEDIA_NO_GESTURE, settings.mediaPlaybackWithoutGesture)
        store.putString(KEY_CACHE_MODE, settings.cacheMode)
        store.putBoolean(KEY_DEFAULT_DESKTOP, settings.defaultDesktopMode)
        store.putBoolean(KEY_DEFAULT_JS, settings.defaultJavaScript)
        store.putBoolean(KEY_AUTO_HIDE_CONTROLS, settings.autoHideControls)
    }

    fun loadNotificationPlayer(): NotificationPlayerSettings = NotificationPlayerSettings(
        enabled = store.getBoolean(KEY_NOTIFICATION_PLAYER_ENABLED, true),
        showPageTitle = store.getBoolean(KEY_NOTIFICATION_PLAYER_TITLE, true)
    )

    fun saveNotificationPlayer(settings: NotificationPlayerSettings) {
        store.putBoolean(KEY_NOTIFICATION_PLAYER_ENABLED, settings.enabled)
        store.putBoolean(KEY_NOTIFICATION_PLAYER_TITLE, settings.showPageTitle)
    }

    fun saveLastWebApp(id: String) = store.putString(KEY_LAST_WEB_APP, id)
    fun loadLastWebApp(): String = store.getString(KEY_LAST_WEB_APP, "")

    fun exportSettingsJson(): String {
        val theme = loadTheme()
        val tv = loadTvMode()
        val browser = loadBrowserControls()
        val notification = loadNotificationPlayer()
        val json = JSONObject()
            .put("theme", JSONObject()
                .put("accentColorHex", theme.accentColorHex)
                .put("glassIntensity", theme.glassIntensity)
                .put("glowIntensity", theme.glowIntensity)
                .put("cardRadius", theme.cardRadius)
                .put("reducedMotion", theme.reducedMotion))
            .put("tvMode", JSONObject()
                .put("deviceMode", tv.deviceMode.name)
                .put("tvLayoutScale", tv.tvLayoutScale)
                .put("showTvControlOverlay", tv.showTvControlOverlay)
                .put("autoHideBrowserControls", tv.autoHideBrowserControls)
                .put("focusGlowIntensity", tv.focusGlowIntensity)
                .put("startInLastWebApp", tv.startInLastWebApp)
                .put("remoteBackBehavior", tv.remoteBackBehavior)
                .put("dpadScrollSpeed", tv.dpadScrollSpeed)
                .put("remoteInputEnabled", tv.remoteInputEnabled)
                .put("enableMediaKeys", tv.enableMediaKeys)
                .put("showLargeCards", tv.showLargeCards)
                .put("preferLandscapeLayout", tv.preferLandscapeLayout))
            .put("browser", JSONObject()
                .put("thirdPartyCookies", browser.thirdPartyCookies)
                .put("mediaPlaybackWithoutGesture", browser.mediaPlaybackWithoutGesture)
                .put("cacheMode", browser.cacheMode)
                .put("defaultDesktopMode", browser.defaultDesktopMode)
                .put("defaultJavaScript", browser.defaultJavaScript)
                .put("autoHideControls", browser.autoHideControls))
            .put("notificationPlayer", JSONObject()
                .put("enabled", notification.enabled)
                .put("showPageTitle", notification.showPageTitle))
        return JsonStorage.prettyObject(json)
    }

    fun importSettingsJson(raw: String): Result<Unit> = runCatching {
        val json = JSONObject(raw)
        json.optJSONObject("theme")?.let {
            saveTheme(ThemeSettings(
                accentColorHex = it.optString("accentColorHex", "#FFFFFF"),
                glassIntensity = it.optDouble("glassIntensity", 0.12).toFloat(),
                glowIntensity = it.optDouble("glowIntensity", 0.80).toFloat(),
                cardRadius = it.optDouble("cardRadius", 26.0).toFloat(),
                reducedMotion = it.optBoolean("reducedMotion", false)
            ))
        }
        json.optJSONObject("tvMode")?.let {
            saveTvMode(TvModeSettings(
                deviceMode = DeviceMode.fromStored(it.optString("deviceMode", DeviceMode.MOBILE.name)),
                tvLayoutScale = it.optString("tvLayoutScale", "comfortable"),
                showTvControlOverlay = it.optBoolean("showTvControlOverlay", true),
                autoHideBrowserControls = it.optBoolean("autoHideBrowserControls", true),
                focusGlowIntensity = it.optDouble("focusGlowIntensity", 0.65).toFloat(),
                startInLastWebApp = it.optBoolean("startInLastWebApp", false),
                remoteBackBehavior = it.optString("remoteBackBehavior", "webview_back_first"),
                dpadScrollSpeed = it.optDouble("dpadScrollSpeed", 1.0).toFloat(),
                remoteInputEnabled = it.optBoolean("remoteInputEnabled", true),
                enableMediaKeys = it.optBoolean("enableMediaKeys", true),
                showLargeCards = it.optBoolean("showLargeCards", true),
                preferLandscapeLayout = it.optBoolean("preferLandscapeLayout", true)
            ))
        }
        json.optJSONObject("notificationPlayer")?.let {
            saveNotificationPlayer(NotificationPlayerSettings(
                enabled = it.optBoolean("enabled", true),
                showPageTitle = it.optBoolean("showPageTitle", true)
            ))
        }
    }

    fun resetAll() = store.clear()

    companion object {
        private const val KEY_ACCENT = "accent_color"
        private const val KEY_GLASS = "glass_intensity"
        private const val KEY_GLOW = "glow_intensity"
        private const val KEY_RADIUS = "card_radius"
        private const val KEY_REDUCED_MOTION = "reduced_motion"
        private const val KEY_DEVICE_MODE = "device_mode"
        private const val KEY_TV_SCALE = "tv_layout_scale"
        private const val KEY_TV_OVERLAY = "tv_overlay"
        private const val KEY_TV_AUTO_HIDE = "tv_auto_hide"
        private const val KEY_TV_FOCUS_GLOW = "tv_focus_glow"
        private const val KEY_START_LAST = "start_in_last_web_app"
        private const val KEY_REMOTE_BACK = "remote_back_behavior"
        private const val KEY_DPAD_SPEED = "dpad_speed"
        private const val KEY_REMOTE_INPUT = "remote_input_enabled"
        private const val KEY_MEDIA_KEYS = "media_keys"
        private const val KEY_LARGE_CARDS = "show_large_cards"
        private const val KEY_LANDSCAPE = "prefer_landscape"
        private const val KEY_THIRD_PARTY_COOKIES = "third_party_cookies"
        private const val KEY_MEDIA_NO_GESTURE = "media_no_gesture"
        private const val KEY_CACHE_MODE = "cache_mode"
        private const val KEY_DEFAULT_DESKTOP = "default_desktop"
        private const val KEY_DEFAULT_JS = "default_js"
        private const val KEY_AUTO_HIDE_CONTROLS = "auto_hide_controls"
        private const val KEY_LAST_WEB_APP = "last_web_app"
        private const val KEY_NOTIFICATION_PLAYER_ENABLED = "notification_player_enabled"
        private const val KEY_NOTIFICATION_PLAYER_TITLE = "notification_player_title"
    }
}
