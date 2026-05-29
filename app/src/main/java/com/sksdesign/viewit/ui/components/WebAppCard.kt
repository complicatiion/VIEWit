package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sksdesign.viewit.R
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.ui.theme.ViewitBackgroundSoft
import com.sksdesign.viewit.ui.theme.ViewitError
import com.sksdesign.viewit.ui.theme.ViewitSuccess
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary

@Composable
fun WebAppCard(
    entry: WebAppEntry,
    themeSettings: ThemeSettings,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onFavorite: () -> Unit,
    onToggleAdblock: () -> Unit,
    onToggleDesktop: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }
    FocusGlowCard(
        themeSettings = themeSettings,
        modifier = modifier.clickable { onOpen() }.onKeyEvent { event ->
            if (event.type == KeyEventType.KeyUp && (event.key == Key.Enter || event.key == Key.DirectionCenter)) {
                onOpen()
                true
            } else false
        }
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(iconFor(entry.iconStyle)),
                    contentDescription = entry.name,
                    colorFilter = ColorFilter.tint(ViewitTextPrimary),
                    modifier = Modifier.size(40.dp)
                )
                Box {
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "Options", tint = ViewitTextPrimary)
                    }
                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = { menuOpen = false },
                        shape = RoundedCornerShape(22.dp),
                        containerColor = ViewitBackgroundSoft.copy(alpha = 0.96f)
                    ) {
                        MenuItem("Open", Icons.Outlined.OpenInBrowser) { menuOpen = false; onOpen() }
                        MenuItem("Edit", Icons.Outlined.Edit) { menuOpen = false; onEdit() }
                        MenuItem("Duplicate", Icons.Outlined.ContentCopy) { menuOpen = false; onDuplicate() }
                        MenuItem(if (entry.favorite) "Unfavorite" else "Favorite", if (entry.favorite) Icons.Outlined.StarBorder else Icons.Outlined.Star) { menuOpen = false; onFavorite() }
                        MenuItem(if (entry.adblockEnabled) "Adblock Off" else "Adblock On", Icons.Outlined.Shield) { menuOpen = false; onToggleAdblock() }
                        MenuItem(if (entry.desktopMode) "Use Mobile" else "Use Desktop", Icons.Outlined.Tv) { menuOpen = false; onToggleDesktop() }
                        MenuItem("Move Up", Icons.Outlined.KeyboardArrowUp) { menuOpen = false; onMoveUp() }
                        MenuItem("Move Down", Icons.Outlined.KeyboardArrowDown) { menuOpen = false; onMoveDown() }
                        if (!entry.builtIn) MenuItem("Delete", Icons.Outlined.Delete, ViewitError) { menuOpen = false; onDelete() }
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            Text(entry.name, color = ViewitTextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            Text(entry.category, color = ViewitTextMuted, fontSize = 13.sp)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                if (entry.favorite) Icon(Icons.Outlined.Star, contentDescription = "Favorite", tint = ViewitTextPrimary, modifier = Modifier.size(18.dp)) else Icon(Icons.Outlined.StarBorder, contentDescription = "Not favorite", tint = ViewitTextMuted, modifier = Modifier.size(18.dp))
                if (entry.adblockEnabled) Icon(Icons.Outlined.Shield, contentDescription = "Adblock enabled", tint = ViewitSuccess, modifier = Modifier.size(18.dp))
                if (entry.desktopMode) Icon(Icons.Outlined.Tv, contentDescription = "Desktop view on mobile", tint = ViewitTextMuted, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun MenuItem(label: String, icon: ImageVector, tint: Color = ViewitTextPrimary, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(label, color = tint, maxLines = 1) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = tint) },
        onClick = onClick
    )
}

fun iconFor(style: String): Int = when (style.lowercase()) {
    "youtube" -> R.drawable.ic_youtube
    "music" -> R.drawable.ic_music
    "soundcloud" -> R.drawable.ic_soundcloud
    "spotify" -> R.drawable.ic_spotify
    "player" -> R.drawable.ic_player
    "video" -> R.drawable.ic_video
    "radio" -> R.drawable.ic_radio
    "news" -> R.drawable.ic_news
    "game" -> R.drawable.ic_game
    "bookmark" -> R.drawable.ic_bookmark_custom
    "dashboard" -> R.drawable.ic_dashboard_custom
    "cloud" -> R.drawable.ic_cloud_custom
    "lock" -> R.drawable.ic_lock_custom
    "globe" -> R.drawable.ic_globe
    else -> R.drawable.ic_web
}
