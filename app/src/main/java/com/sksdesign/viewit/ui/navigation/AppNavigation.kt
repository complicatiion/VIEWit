package com.sksdesign.viewit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.sksdesign.viewit.adblock.AdblockEngine
import com.sksdesign.viewit.adblock.NoOpAdblockEngine
import com.sksdesign.viewit.adblock.SimpleHostBlockEngine
import com.sksdesign.viewit.data.model.AdblockSettings
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.data.repository.AdblockRepository
import com.sksdesign.viewit.data.repository.PrivacyRepository
import com.sksdesign.viewit.data.repository.SettingsRepository
import com.sksdesign.viewit.data.repository.WebLibraryRepository
import com.sksdesign.viewit.device.LayoutModeResolver
import com.sksdesign.viewit.ui.screens.AboutScreen
import com.sksdesign.viewit.ui.screens.AddWebAppScreen
import com.sksdesign.viewit.ui.screens.AdblockSettingsScreen
import com.sksdesign.viewit.ui.screens.BrowserScreen
import com.sksdesign.viewit.ui.screens.HomeScreen
import com.sksdesign.viewit.ui.screens.PrivacyDnsScreen
import com.sksdesign.viewit.ui.screens.SettingsScreen
import com.sksdesign.viewit.ui.tv.TvBrowserScreen
import com.sksdesign.viewit.ui.tv.TvHomeScreen
import com.sksdesign.viewit.ui.tv.TvSettingsScreen

private sealed class AppRoute {
    data object Home : AppRoute()
    data object Settings : AppRoute()
    data object AddWebApp : AppRoute()
    data object Adblocking : AppRoute()
    data object PrivacyDns : AppRoute()
    data object About : AppRoute()
    data class Browser(val entry: WebAppEntry) : AppRoute()
}

@Composable
fun AppNavigation(
    webLibraryRepository: WebLibraryRepository,
    settingsRepository: SettingsRepository,
    adblockRepository: AdblockRepository,
    privacyRepository: PrivacyRepository
) {
    val context = LocalContext.current
    var entries by remember { mutableStateOf(webLibraryRepository.loadWebApps()) }
    var themeSettings by remember { mutableStateOf(settingsRepository.loadTheme()) }
    var tvModeSettings by remember { mutableStateOf(settingsRepository.loadTvMode()) }
    var browserSettings by remember { mutableStateOf(settingsRepository.loadBrowserControls()) }
    var notificationSettings by remember { mutableStateOf(settingsRepository.loadNotificationPlayer()) }
    var adblockSettings by remember { mutableStateOf(adblockRepository.load()) }
    var selectedDnsProfile by remember { mutableStateOf(privacyRepository.selectedProfileId()) }
    var customDnsValues by remember { mutableStateOf(privacyRepository.customDnsValues()) }
    var route by remember { mutableStateOf<AppRoute>(AppRoute.Home) }
    var editEntry by remember { mutableStateOf<WebAppEntry?>(null) }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val useTvLayout = LayoutModeResolver.shouldUseTvLayout(context, tvModeSettings)
    val adblockEngine: AdblockEngine = remember(adblockSettings) { buildAdblockEngine(adblockSettings) }
    val visibleEntries = if (showFavoritesOnly) entries.filter { it.favorite } else entries

    when (val current = route) {
        AppRoute.Home -> {
            if (useTvLayout) {
                TvHomeScreen(
                    entries = visibleEntries,
                    themeSettings = themeSettings,
                    tvModeSettings = tvModeSettings,
                    showFavoritesOnly = showFavoritesOnly,
                    onToggleFavoritesOnly = { showFavoritesOnly = !showFavoritesOnly },
                    onOpen = { entry -> settingsRepository.saveLastWebApp(entry.id); route = AppRoute.Browser(entry) },
                    onAdd = { editEntry = null; route = AppRoute.AddWebApp },
                    onSettings = { route = AppRoute.Settings }
                )
            } else {
                HomeScreen(
                    entries = visibleEntries,
                    themeSettings = themeSettings,
                    showFavoritesOnly = showFavoritesOnly,
                    onToggleFavoritesOnly = { showFavoritesOnly = !showFavoritesOnly },
                    onOpen = { entry -> settingsRepository.saveLastWebApp(entry.id); route = AppRoute.Browser(entry) },
                    onAdd = { editEntry = null; route = AppRoute.AddWebApp },
                    onSettings = { route = AppRoute.Settings },
                    onEdit = { editEntry = it; route = AppRoute.AddWebApp },
                    onDuplicate = { entries = webLibraryRepository.duplicate(it) },
                    onDelete = { entries = webLibraryRepository.delete(it) },
                    onFavorite = { entries = webLibraryRepository.toggleFavorite(it) },
                    onToggleAdblock = { entries = webLibraryRepository.toggleAdblock(it) },
                    onToggleDesktop = { entries = webLibraryRepository.toggleDesktopMode(it) },
                    onMoveUp = { entries = webLibraryRepository.moveUp(it) },
                    onMoveDown = { entries = webLibraryRepository.moveDown(it) }
                )
            }
        }
        AppRoute.Settings -> {
            val settingsScreen: @Composable () -> Unit = {
                SettingsScreen(
                    themeSettings = themeSettings,
                    tvModeSettings = tvModeSettings,
                    browserSettings = browserSettings,
                    notificationSettings = notificationSettings,
                    webLibraryJson = webLibraryRepository.exportJson(),
                    settingsJson = settingsRepository.exportSettingsJson(),
                    onThemeChanged = { themeSettings = it; settingsRepository.saveTheme(it) },
                    onTvModeChanged = { tvModeSettings = it; settingsRepository.saveTvMode(it) },
                    onBrowserChanged = { browserSettings = it; settingsRepository.saveBrowserControls(it) },
                    onNotificationChanged = { notificationSettings = it; settingsRepository.saveNotificationPlayer(it) },
                    onResetWebLibrary = { entries = webLibraryRepository.reset(); showFavoritesOnly = false },
                    onResetAppSettings = {
                        settingsRepository.resetAll()
                        entries = webLibraryRepository.loadWebApps()
                        themeSettings = settingsRepository.loadTheme()
                        tvModeSettings = settingsRepository.loadTvMode()
                        browserSettings = settingsRepository.loadBrowserControls()
                        notificationSettings = settingsRepository.loadNotificationPlayer()
                        adblockSettings = adblockRepository.load()
                        selectedDnsProfile = privacyRepository.selectedProfileId()
                        customDnsValues = privacyRepository.customDnsValues()
                        showFavoritesOnly = false
                    },
                    onSortWebLibrary = { entries = webLibraryRepository.sortByName() },
                    onImportWebLibrary = { raw ->
                        webLibraryRepository.importJson(raw).fold(
                            onSuccess = { imported -> entries = imported; "Weblibrary imported." },
                            onFailure = { error -> error.message ?: "Import failed." }
                        )
                    },
                    onImportSettings = { raw ->
                        settingsRepository.importSettingsJson(raw).fold(
                            onSuccess = {
                                themeSettings = settingsRepository.loadTheme()
                                tvModeSettings = settingsRepository.loadTvMode()
                                browserSettings = settingsRepository.loadBrowserControls()
                                notificationSettings = settingsRepository.loadNotificationPlayer()
                                "Settings imported."
                            },
                            onFailure = { error -> error.message ?: "Import failed." }
                        )
                    },
                    onOpenAdblocking = { route = AppRoute.Adblocking },
                    onOpenPrivacyDns = { route = AppRoute.PrivacyDns },
                    onOpenAbout = { route = AppRoute.About },
                    onBack = { route = AppRoute.Home }
                )
            }
            if (useTvLayout) {
                TvSettingsScreen(
                    themeSettings = themeSettings,
                    tvModeSettings = tvModeSettings,
                    browserSettings = browserSettings,
                    notificationSettings = notificationSettings,
                    webLibraryJson = webLibraryRepository.exportJson(),
                    settingsJson = settingsRepository.exportSettingsJson(),
                    onThemeChanged = { themeSettings = it; settingsRepository.saveTheme(it) },
                    onTvModeChanged = { tvModeSettings = it; settingsRepository.saveTvMode(it) },
                    onBrowserChanged = { browserSettings = it; settingsRepository.saveBrowserControls(it) },
                    onNotificationChanged = { notificationSettings = it; settingsRepository.saveNotificationPlayer(it) },
                    onResetWebLibrary = { entries = webLibraryRepository.reset(); showFavoritesOnly = false },
                    onResetAppSettings = {
                        settingsRepository.resetAll()
                        entries = webLibraryRepository.loadWebApps()
                        themeSettings = settingsRepository.loadTheme()
                        tvModeSettings = settingsRepository.loadTvMode()
                        browserSettings = settingsRepository.loadBrowserControls()
                        notificationSettings = settingsRepository.loadNotificationPlayer()
                        adblockSettings = adblockRepository.load()
                        selectedDnsProfile = privacyRepository.selectedProfileId()
                        customDnsValues = privacyRepository.customDnsValues()
                        showFavoritesOnly = false
                    },
                    onSortWebLibrary = { entries = webLibraryRepository.sortByName() },
                    onImportWebLibrary = { raw ->
                        webLibraryRepository.importJson(raw).fold(
                            onSuccess = { imported -> entries = imported; "Weblibrary imported." },
                            onFailure = { error -> error.message ?: "Import failed." }
                        )
                    },
                    onImportSettings = { raw ->
                        settingsRepository.importSettingsJson(raw).fold(
                            onSuccess = {
                                themeSettings = settingsRepository.loadTheme()
                                tvModeSettings = settingsRepository.loadTvMode()
                                browserSettings = settingsRepository.loadBrowserControls()
                                notificationSettings = settingsRepository.loadNotificationPlayer()
                                "Settings imported."
                            },
                            onFailure = { error -> error.message ?: "Import failed." }
                        )
                    },
                    onOpenAdblocking = { route = AppRoute.Adblocking },
                    onOpenPrivacyDns = { route = AppRoute.PrivacyDns },
                    onOpenAbout = { route = AppRoute.About },
                    onBack = { route = AppRoute.Home }
                )
            } else settingsScreen()
        }
        AppRoute.AddWebApp -> AddWebAppScreen(
            themeSettings = themeSettings,
            initialEntry = editEntry,
            onSave = { entry -> entries = webLibraryRepository.upsert(entry); route = AppRoute.Home },
            onBack = { route = AppRoute.Home }
        )
        AppRoute.Adblocking -> AdblockSettingsScreen(
            themeSettings = themeSettings,
            settings = adblockSettings,
            onSave = { updated -> adblockSettings = updated; adblockRepository.save(updated) },
            onBack = { route = AppRoute.Settings }
        )
        AppRoute.PrivacyDns -> PrivacyDnsScreen(
            themeSettings = themeSettings,
            profiles = privacyRepository.profiles(),
            selectedProfileId = selectedDnsProfile,
            customDnsValues = customDnsValues,
            onSelectProfile = { selectedDnsProfile = it; privacyRepository.saveSelectedProfile(it) },
            onSaveCustomDns = { customDnsValues = it; privacyRepository.saveCustomDnsValues(it) },
            onBack = { route = AppRoute.Settings }
        )
        AppRoute.About -> AboutScreen(themeSettings = themeSettings, onBack = { route = AppRoute.Home })
        is AppRoute.Browser -> {
            if (useTvLayout) {
                TvBrowserScreen(
                    entry = current.entry,
                    themeSettings = themeSettings,
                    tvModeSettings = tvModeSettings,
                    browserSettings = browserSettings,
                    notificationSettings = notificationSettings,
                    adblockEngine = adblockEngine,
                    onClose = { route = AppRoute.Home },
                    onMainMenu = { route = AppRoute.Home }
                )
            } else {
                BrowserScreen(
                    entry = current.entry,
                    themeSettings = themeSettings,
                    browserSettings = browserSettings,
                    notificationSettings = notificationSettings,
                    adblockEngine = adblockEngine,
                    onClose = { route = AppRoute.Home },
                    onMainMenu = { route = AppRoute.Home },
                    onUpdateEntry = { updated -> entries = webLibraryRepository.upsert(updated) }
                )
            }
        }
    }
}

private fun buildAdblockEngine(settings: AdblockSettings): AdblockEngine {
    return if (!settings.globalEnabled) {
        NoOpAdblockEngine()
    } else {
        SimpleHostBlockEngine(enabled = settings.useSimpleHostBlockEngine, initialRules = settings.userRules)
    }
}
