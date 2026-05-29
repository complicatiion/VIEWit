package com.sksdesign.viewit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.sksdesign.viewit.data.repository.AdblockRepository
import com.sksdesign.viewit.data.repository.PrivacyRepository
import com.sksdesign.viewit.data.repository.SettingsRepository
import com.sksdesign.viewit.data.repository.WebLibraryRepository
import com.sksdesign.viewit.ui.navigation.AppNavigation

@Composable
fun ViewitApp() {
    val context = LocalContext.current
    val webLibraryRepository = remember { WebLibraryRepository(context) }
    val settingsRepository = remember { SettingsRepository(context) }
    val adblockRepository = remember { AdblockRepository(context) }
    val privacyRepository = remember { PrivacyRepository(context) }

    AppNavigation(
        webLibraryRepository = webLibraryRepository,
        settingsRepository = settingsRepository,
        adblockRepository = adblockRepository,
        privacyRepository = privacyRepository
    )
}
