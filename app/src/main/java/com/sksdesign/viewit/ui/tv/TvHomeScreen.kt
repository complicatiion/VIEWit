package com.sksdesign.viewit.ui.tv

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.TvModeSettings
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.ui.components.CyberBackground
import com.sksdesign.viewit.ui.components.RemoteFocusableButton
import com.sksdesign.viewit.ui.components.ViewitLogo
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary

@Composable
fun TvHomeScreen(
    entries: List<WebAppEntry>,
    themeSettings: ThemeSettings,
    tvModeSettings: TvModeSettings,
    showFavoritesOnly: Boolean,
    onToggleFavoritesOnly: () -> Unit,
    onOpen: (WebAppEntry) -> Unit,
    onAdd: () -> Unit,
    onSettings: () -> Unit
) {
    val spec = TvHomeSpec.from(tvModeSettings.tvLayoutScale)
    CyberBackground() {
        Column(Modifier.fillMaxSize().padding(horizontal = spec.screenPadding, vertical = spec.verticalPadding)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                ViewitLogo(spec.logoSize)
                Text("VIEWit", color = ViewitTextPrimary, fontWeight = FontWeight.Bold, fontSize = spec.titleSize, modifier = Modifier.padding(start = 14.dp))
                Spacer(Modifier.weight(1f))
                RemoteFocusableButton("Home", themeSettings, compact = spec.compactButtons, icon = Icons.Outlined.Home, onClick = {})
                Spacer(Modifier.width(spec.buttonGap))
                RemoteFocusableButton(if (showFavoritesOnly) "Favorites" else "All", themeSettings, compact = spec.compactButtons, icon = if (showFavoritesOnly) Icons.Outlined.Star else Icons.Outlined.StarBorder, onClick = onToggleFavoritesOnly)
                Spacer(Modifier.width(spec.buttonGap))
                RemoteFocusableButton("Add", themeSettings, compact = spec.compactButtons, icon = Icons.Outlined.Add, onClick = onAdd)
                Spacer(Modifier.width(spec.buttonGap))
                RemoteFocusableButton("Settings", themeSettings, compact = spec.compactButtons, icon = Icons.Outlined.Settings, onClick = onSettings)
            }
            Spacer(Modifier.height(spec.rowTopGap))
            if (entries.isEmpty()) {
                Text("No favorite web apps selected.", color = ViewitTextMuted, modifier = Modifier.padding(top = 24.dp))
            } else {
                TvGrid(entries, themeSettings, tvModeSettings.remoteInputEnabled, spec, onOpen)
            }
        }
    }
}

@Composable
private fun TvGrid(
    entries: List<WebAppEntry>,
    themeSettings: ThemeSettings,
    remoteInputEnabled: Boolean,
    spec: TvHomeSpec,
    onOpen: (WebAppEntry) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(spec.cardGap),
        verticalArrangement = Arrangement.spacedBy(spec.cardGap),
        contentPadding = PaddingValues(start = 4.dp, top = 4.dp, end = 4.dp, bottom = spec.bottomPadding),
        modifier = Modifier.fillMaxSize().focusGroup()
    ) {
        items(entries, key = { it.id }) { entry ->
            TvWebAppCard(
                entry = entry,
                themeSettings = themeSettings,
                remoteInputEnabled = remoteInputEnabled,
                modifier = Modifier.fillMaxWidth().height(spec.cardHeight)
            ) { onOpen(entry) }
        }
    }
}

private data class TvHomeSpec(
    val screenPadding: Dp,
    val verticalPadding: Dp,
    val logoSize: Dp,
    val titleSize: androidx.compose.ui.unit.TextUnit,
    val cardHeight: Dp,
    val cardGap: Dp,
    val buttonGap: Dp,
    val rowTopGap: Dp,
    val bottomPadding: Dp,
    val compactButtons: Boolean
) {
    companion object {
        fun from(scale: String): TvHomeSpec = when (scale.lowercase()) {
            "compact" -> TvHomeSpec(20.dp, 16.dp, 40.dp, 26.sp, 132.dp, 10.dp, 8.dp, 16.dp, 28.dp, true)
            "large" -> TvHomeSpec(30.dp, 22.dp, 52.dp, 34.sp, 164.dp, 14.dp, 10.dp, 22.dp, 34.dp, false)
            else -> TvHomeSpec(24.dp, 20.dp, 48.dp, 30.sp, 146.dp, 12.dp, 8.dp, 18.dp, 30.dp, true)
        }
    }
}
