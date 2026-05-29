package com.sksdesign.viewit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.ui.components.CyberBackground
import com.sksdesign.viewit.ui.components.NeonIconButton
import com.sksdesign.viewit.ui.components.ViewitLogo
import com.sksdesign.viewit.ui.components.WebAppCard
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary

@Composable
fun HomeScreen(
    entries: List<WebAppEntry>,
    themeSettings: ThemeSettings,
    showFavoritesOnly: Boolean,
    onToggleFavoritesOnly: () -> Unit,
    onOpen: (WebAppEntry) -> Unit,
    onAdd: () -> Unit,
    onSettings: () -> Unit,
    onEdit: (WebAppEntry) -> Unit,
    onDuplicate: (WebAppEntry) -> Unit,
    onDelete: (WebAppEntry) -> Unit,
    onFavorite: (WebAppEntry) -> Unit,
    onToggleAdblock: (WebAppEntry) -> Unit,
    onToggleDesktop: (WebAppEntry) -> Unit,
    onMoveUp: (WebAppEntry) -> Unit,
    onMoveDown: (WebAppEntry) -> Unit
) {
    CyberBackground() {
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                ViewitLogo(44.dp)
                Text("VIEWit", color = ViewitTextPrimary, fontWeight = FontWeight.Bold, fontSize = 28.sp, modifier = Modifier.padding(start = 12.dp))
                Spacer(Modifier.weight(1f))
                NeonIconButton(if (showFavoritesOnly) Icons.Outlined.Star else Icons.Outlined.StarBorder, "Favorites", themeSettings, onClick = onToggleFavoritesOnly)
                Spacer(Modifier.padding(5.dp))
                NeonIconButton(Icons.Outlined.Add, "Add Web App", themeSettings, onClick = onAdd)
                Spacer(Modifier.padding(5.dp))
                NeonIconButton(Icons.Outlined.Settings, "Settings", themeSettings, onClick = onSettings)
            }
            Spacer(Modifier.height(18.dp))
            if (entries.isEmpty()) {
                Text("No favorite web apps selected.", color = ViewitTextMuted, modifier = Modifier.padding(top = 24.dp))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 184.dp),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(entries, key = { it.id }) { entry ->
                        WebAppCard(
                            entry = entry,
                            themeSettings = themeSettings,
                            modifier = Modifier.height(210.dp),
                            onOpen = { onOpen(entry) },
                            onEdit = { onEdit(entry) },
                            onDuplicate = { onDuplicate(entry) },
                            onDelete = { onDelete(entry) },
                            onFavorite = { onFavorite(entry) },
                            onToggleAdblock = { onToggleAdblock(entry) },
                            onToggleDesktop = { onToggleDesktop(entry) },
                            onMoveUp = { onMoveUp(entry) },
                            onMoveDown = { onMoveDown(entry) }
                        )
                    }
                }
            }
        }
    }
}
