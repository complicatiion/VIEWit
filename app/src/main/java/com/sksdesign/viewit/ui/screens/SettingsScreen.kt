package com.sksdesign.viewit.ui.screens

import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.BrowserControlSettings
import com.sksdesign.viewit.data.model.DeviceMode
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.NotificationPlayerSettings
import com.sksdesign.viewit.data.model.TvModeSettings
import com.sksdesign.viewit.ui.components.CyberBackground
import com.sksdesign.viewit.ui.components.GlassCard
import com.sksdesign.viewit.ui.components.GlowButton
import com.sksdesign.viewit.ui.components.SectionHeader
import com.sksdesign.viewit.ui.components.ViewitChoiceRow
import com.sksdesign.viewit.ui.components.ViewitLogo
import com.sksdesign.viewit.ui.components.ViewitOutlinedTextField
import com.sksdesign.viewit.ui.components.ViewitSliderRow
import com.sksdesign.viewit.ui.components.ViewitSwitchRow
import com.sksdesign.viewit.ui.theme.ViewitBackgroundSoft
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary
import com.sksdesign.viewit.util.ClipboardUtils
import com.sksdesign.viewit.util.ColorUtils

@Composable
fun SettingsScreen(
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
    val context = LocalContext.current
    var importTitle by remember { mutableStateOf("") }
    var importText by remember { mutableStateOf("") }
    var importAction by remember { mutableStateOf<((String) -> String)?>(null) }
    var importResult by remember { mutableStateOf("") }
    var savedMessage by remember { mutableStateOf("") }
    var showResetConfirm by remember { mutableStateOf(false) }

    BackHandler { onBack() }

    CyberBackground() {
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ViewitLogo(40.dp, onClick = onBack)
                Spacer(Modifier.padding(6.dp))
                SectionHeader("Settings")
                Spacer(Modifier.weight(1f))
                GlowButton("Save", themeSettings, icon = Icons.Outlined.Save, compact = true) { savedMessage = "Settings saved." }
                Spacer(Modifier.padding(4.dp))
                GlowButton("Back", themeSettings, compact = true, onClick = onBack)
            }
            if (savedMessage.isNotBlank()) Text(savedMessage, color = ViewitTextPrimary)

            SettingsSection(themeSettings, "Adblocking", Icons.Outlined.Security) {
                Text("Adblocking is best effort. Some dynamic web apps such as YouTube, Spotify or other streaming services may still show ads.", color = ViewitTextMuted)
                GlowButton("Adblock Settings", themeSettings, icon = Icons.Outlined.Security, modifier = Modifier.fillMaxWidth(), onClick = onOpenAdblocking)
            }

            SettingsSection(themeSettings, "Privacy DNS", Icons.Outlined.Dns) {
                Text("VIEWit only displays DNS values and does not change system DNS settings automatically.", color = ViewitTextMuted)
                GlowButton("Privacy DNS", themeSettings, icon = Icons.Outlined.Dns, modifier = Modifier.fillMaxWidth(), onClick = onOpenPrivacyDns)
            }

            SettingsSection(themeSettings, "TV Mode", Icons.Outlined.Tv) {
                ViewitChoiceRow("Device Mode", tvModeSettings.deviceMode.name, listOf(DeviceMode.MOBILE.name, DeviceMode.TV.name), themeSettings, Icons.Outlined.Tv) { onTvModeChanged(tvModeSettings.copy(deviceMode = DeviceMode.fromStored(it))) }
                ViewitChoiceRow("TV Layout Scale", tvModeSettings.tvLayoutScale, listOf("compact", "comfortable", "large"), themeSettings, Icons.Outlined.Tv) { onTvModeChanged(tvModeSettings.copy(tvLayoutScale = it)) }
                ViewitSwitchRow("Show TV Control Overlay", tvModeSettings.showTvControlOverlay) { onTvModeChanged(tvModeSettings.copy(showTvControlOverlay = it)) }
                ViewitSwitchRow("Auto Hide Browser Controls", tvModeSettings.autoHideBrowserControls) { onTvModeChanged(tvModeSettings.copy(autoHideBrowserControls = it)) }
                ViewitSliderRow("Focus Glow Intensity", tvModeSettings.focusGlowIntensity, 0.10f..1.0f) { onTvModeChanged(tvModeSettings.copy(focusGlowIntensity = it)) }
                ViewitSwitchRow("Start in Last Web App", tvModeSettings.startInLastWebApp) { onTvModeChanged(tvModeSettings.copy(startInLastWebApp = it)) }
                ViewitChoiceRow("Remote Back", tvModeSettings.remoteBackBehavior, listOf("webview_back_first", "always_show_controls", "return_home"), themeSettings) { onTvModeChanged(tvModeSettings.copy(remoteBackBehavior = it)) }
                ViewitSliderRow("D-Pad Scroll Speed", tvModeSettings.dpadScrollSpeed, 0.25f..3.0f) { onTvModeChanged(tvModeSettings.copy(dpadScrollSpeed = it)) }
                ViewitSwitchRow("Remote / Keyboard Input", tvModeSettings.remoteInputEnabled) { onTvModeChanged(tvModeSettings.copy(remoteInputEnabled = it)) }
                ViewitSwitchRow("Enable Media Keys", tvModeSettings.enableMediaKeys) { onTvModeChanged(tvModeSettings.copy(enableMediaKeys = it)) }
                ViewitSwitchRow("Show Large Cards", tvModeSettings.showLargeCards) { onTvModeChanged(tvModeSettings.copy(showLargeCards = it)) }
                ViewitSwitchRow("Prefer Landscape Layout", tvModeSettings.preferLandscapeLayout) { onTvModeChanged(tvModeSettings.copy(preferLandscapeLayout = it)) }
            }

            SettingsSection(themeSettings, "Notification Player", Icons.Outlined.Notifications) {
                Text("Show Android notification controls for the active VIEWit WebView. Media actions are best effort because each web app handles playback differently.", color = ViewitTextMuted)
                ViewitSwitchRow("Enable Notification Player", notificationSettings.enabled) { onNotificationChanged(notificationSettings.copy(enabled = it)) }
                ViewitSwitchRow("Show Page Title", notificationSettings.showPageTitle) { onNotificationChanged(notificationSettings.copy(showPageTitle = it)) }
            }

            SettingsSection(themeSettings, "WebView", Icons.Outlined.Code) {
                ViewitSwitchRow("JavaScript Defaults", browserSettings.defaultJavaScript) { onBrowserChanged(browserSettings.copy(defaultJavaScript = it)) }
                ViewitSwitchRow("Desktop View Defaults", browserSettings.defaultDesktopMode) { onBrowserChanged(browserSettings.copy(defaultDesktopMode = it)) }
                ViewitSwitchRow("Third-Party Cookies", browserSettings.thirdPartyCookies) { onBrowserChanged(browserSettings.copy(thirdPartyCookies = it)) }
                ViewitChoiceRow("Cache Mode", browserSettings.cacheMode, listOf("default", "no_cache", "cache_else_network"), themeSettings, Icons.Outlined.Cached) { onBrowserChanged(browserSettings.copy(cacheMode = it)) }
            }

            SettingsSection(themeSettings, "Weblibrary", Icons.Outlined.Code) {
                GlowButton("Reset Library", themeSettings, icon = Icons.Outlined.RestartAlt, modifier = Modifier.fillMaxWidth(), onClick = onResetWebLibrary)
                GlowButton("Export Library", themeSettings, icon = Icons.Outlined.Download, modifier = Modifier.fillMaxWidth()) { ClipboardUtils.copy(context, "VIEWit Weblibrary", webLibraryJson) }
                GlowButton("Import Library", themeSettings, icon = Icons.Outlined.Upload, modifier = Modifier.fillMaxWidth()) { importTitle = "Import Weblibrary JSON"; importText = ""; importAction = onImportWebLibrary }
            }

            SettingsSection(themeSettings, "Data & Cache", Icons.Outlined.DeleteSweep) {
                GlowButton("Clear Cache", themeSettings, icon = Icons.Outlined.DeleteSweep, modifier = Modifier.fillMaxWidth()) { WebView(context).clearCache(true) }
                GlowButton("Clear Cookies", themeSettings, icon = Icons.Outlined.DeleteSweep, modifier = Modifier.fillMaxWidth()) { CookieManager.getInstance().removeAllCookies(null); CookieManager.getInstance().flush() }
                GlowButton("Export Settings", themeSettings, icon = Icons.Outlined.Download, modifier = Modifier.fillMaxWidth()) { ClipboardUtils.copy(context, "VIEWit Settings", settingsJson) }
                GlowButton("Import Settings", themeSettings, icon = Icons.Outlined.Upload, modifier = Modifier.fillMaxWidth()) { importTitle = "Import Settings JSON"; importText = ""; importAction = onImportSettings }
                GlowButton("Reset App", themeSettings, icon = Icons.Outlined.SettingsBackupRestore, modifier = Modifier.fillMaxWidth()) { showResetConfirm = true }
            }

            SettingsSection(themeSettings, "Appearance", Icons.Outlined.Palette) {
                var accent by remember(themeSettings.accentColorHex) { mutableStateOf(themeSettings.accentColorHex) }
                ViewitOutlinedTextField(value = accent, onValueChange = { accent = it }, label = "Accent Color", modifier = Modifier.fillMaxWidth())
                GlowButton("Save Accent", themeSettings, icon = Icons.Outlined.Check, modifier = Modifier.fillMaxWidth()) { onThemeChanged(themeSettings.copy(accentColorHex = ColorUtils.normalizeHex(accent))); savedMessage = "Accent color saved." }
                ViewitSliderRow("Glass Intensity", themeSettings.glassIntensity, 0.04f..0.30f) { onThemeChanged(themeSettings.copy(glassIntensity = it)) }
                ViewitSliderRow("Glow Intensity", themeSettings.glowIntensity, 0.10f..1.0f) { onThemeChanged(themeSettings.copy(glowIntensity = it)) }
                ViewitSliderRow("Card Radius", themeSettings.cardRadius, 12f..40f) { onThemeChanged(themeSettings.copy(cardRadius = it)) }
                ViewitSwitchRow("Reduced Motion", themeSettings.reducedMotion) { onThemeChanged(themeSettings.copy(reducedMotion = it)) }
            }

            SettingsSection(themeSettings, "About", Icons.Outlined.Info) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ViewitLogo(62.dp)
                    Column(Modifier.weight(1f)) {
                        Text("VIEWit", color = ViewitTextPrimary)
                        Text("Version: V1.1.2", color = ViewitTextMuted)
                        Text("Author: complicatiion aka sksdesign", color = ViewitTextMuted)
                        Text("Third-party components and licenses are listed in About.", color = ViewitTextMuted)
                    }
                }
                GlowButton("Open About", themeSettings, icon = Icons.Outlined.Info, modifier = Modifier.fillMaxWidth(), onClick = onOpenAbout)
            }
        }
    }

    if (importAction != null) {
        AlertDialog(
            onDismissRequest = { importAction = null },
            containerColor = ViewitBackgroundSoft,
            titleContentColor = ViewitTextPrimary,
            textContentColor = ViewitTextPrimary,
            title = { Text(importTitle, color = ViewitTextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ViewitOutlinedTextField(value = importText, onValueChange = { importText = it }, label = "Paste JSON", minLines = 8, modifier = Modifier.fillMaxWidth())
                    if (importResult.isNotBlank()) Text(importResult, color = ViewitTextPrimary)
                }
            },
            confirmButton = { TextButton(onClick = { importResult = importAction?.invoke(importText).orEmpty() }) { Text("Import", color = ViewitTextPrimary) } },
            dismissButton = { TextButton(onClick = { importAction = null }) { Text("Close", color = ViewitTextPrimary) } }
        )
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            containerColor = ViewitBackgroundSoft,
            titleContentColor = ViewitTextPrimary,
            textContentColor = ViewitTextPrimary,
            title = { Text("Reset VIEWit", color = ViewitTextPrimary) },
            text = { Text("This clears local VIEWit settings, Weblibrary entries and stored preferences. WebView cookies can be cleared separately.", color = ViewitTextPrimary) },
            confirmButton = {
                TextButton(onClick = { showResetConfirm = false; onResetAppSettings(); savedMessage = "VIEWit settings reset." }) { Text("Reset", color = ViewitTextPrimary) }
            },
            dismissButton = { TextButton(onClick = { showResetConfirm = false }) { Text("Cancel", color = ViewitTextPrimary) } }
        )
    }
}

@Composable
private fun SettingsSection(themeSettings: ThemeSettings, title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable ColumnScope.() -> Unit) {
    GlassCard(themeSettings, Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                androidx.compose.material3.Icon(icon, contentDescription = null, tint = ViewitTextPrimary)
                SectionHeader(title)
            }
            Divider(color = Color.White.copy(alpha = 0.10f))
            content()
        }
    }
}
