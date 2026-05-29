package com.sksdesign.viewit.ui.tv

import androidx.compose.runtime.Composable
import com.sksdesign.viewit.data.model.BrowserControlSettings
import com.sksdesign.viewit.data.model.NotificationPlayerSettings
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.TvModeSettings
import com.sksdesign.viewit.ui.screens.SettingsScreen

@Composable
fun TvSettingsScreen(
    themeSettings: ThemeSettings,
    tvModeSettings: TvModeSettings,
    browserSettings: BrowserControlSettings,
    notificationSettings: NotificationPlayerSettings,
    webLibraryJson: String,
    settingsJson: String,
    onThemeChanged: (ThemeSettings) -> Unit,
    onTvModeChanged: (TvModeSettings) -> Unit,
    onBrowserChanged: (BrowserControlSettings) -> Unit,
    onNotificationChanged: (NotificationPlayerSettings) -> Unit,
    onResetWebLibrary: () -> Unit,
    onResetAppSettings: () -> Unit,
    onSortWebLibrary: () -> Unit,
    onImportWebLibrary: (String) -> String,
    onImportSettings: (String) -> String,
    onOpenAdblocking: () -> Unit,
    onOpenPrivacyDns: () -> Unit,
    onOpenAbout: () -> Unit,
    onBack: () -> Unit
) {
    SettingsScreen(
        themeSettings = themeSettings,
        tvModeSettings = tvModeSettings,
        browserSettings = browserSettings,
        notificationSettings = notificationSettings,
        webLibraryJson = webLibraryJson,
        settingsJson = settingsJson,
        onThemeChanged = onThemeChanged,
        onTvModeChanged = onTvModeChanged,
        onBrowserChanged = onBrowserChanged,
        onNotificationChanged = onNotificationChanged,
        onResetWebLibrary = onResetWebLibrary,
        onResetAppSettings = onResetAppSettings,
        onSortWebLibrary = onSortWebLibrary,
        onImportWebLibrary = onImportWebLibrary,
        onImportSettings = onImportSettings,
        onOpenAdblocking = onOpenAdblocking,
        onOpenPrivacyDns = onOpenPrivacyDns,
        onOpenAbout = onOpenAbout,
        onBack = onBack
    )
}
