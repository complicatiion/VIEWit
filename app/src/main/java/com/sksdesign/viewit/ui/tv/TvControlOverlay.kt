package com.sksdesign.viewit.ui.tv

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.ui.components.RemoteFocusableButton

@Composable
fun TvControlOverlay(
    themeSettings: ThemeSettings,
    modifier: Modifier = Modifier,
    onMainMenu: () -> Unit,
    onHome: () -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onReload: () -> Unit,
    onExternal: () -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 18.dp, vertical = 20.dp)
            .widthIn(max = 1040.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RemoteFocusableButton("Home", themeSettings, icon = Icons.Outlined.Apps, compact = true, onClick = onMainMenu)
        RemoteFocusableButton("Menu", themeSettings, icon = Icons.Outlined.Home, compact = true, onClick = onHome)
        RemoteFocusableButton("Back", themeSettings, icon = Icons.Outlined.ArrowBack, compact = true, onClick = onBack)
        RemoteFocusableButton("Forward", themeSettings, icon = Icons.Outlined.ArrowForward, compact = true, onClick = onForward)
        RemoteFocusableButton("Reload", themeSettings, icon = Icons.Outlined.Refresh, compact = true, onClick = onReload)
        RemoteFocusableButton("External", themeSettings, icon = Icons.Outlined.OpenInBrowser, compact = true, onClick = onExternal)
        RemoteFocusableButton("Close", themeSettings, icon = Icons.Outlined.Close, compact = true, onClick = onClose)
    }
}
