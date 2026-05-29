package com.sksdesign.viewit.ui.tv

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.ui.theme.ViewitSuccess
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary
import com.sksdesign.viewit.ui.components.iconFor

@Composable
fun TvWebAppCard(entry: WebAppEntry, themeSettings: ThemeSettings, remoteInputEnabled: Boolean, modifier: Modifier = Modifier, onOpen: () -> Unit) {
    TvFocusGlow(
        themeSettings = themeSettings,
        contentPadding = PaddingValues(14.dp),
        modifier = modifier.clickable { onOpen() }.onKeyEvent { event ->
            if (remoteInputEnabled && event.type == KeyEventType.KeyUp && (event.key == Key.Enter || event.key == Key.DirectionCenter)) {
                onOpen()
                true
            } else false
        }
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(iconFor(entry.iconStyle)), contentDescription = entry.name, colorFilter = ColorFilter.tint(ViewitTextPrimary), modifier = Modifier.size(42.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (entry.favorite) Icon(Icons.Outlined.Star, contentDescription = "Favorite", tint = ViewitTextPrimary)
                    if (entry.adblockEnabled) Icon(Icons.Outlined.Shield, contentDescription = "Adblock", tint = ViewitSuccess)
                    if (entry.desktopMode) Icon(Icons.Outlined.Tv, contentDescription = "Desktop view", tint = ViewitTextMuted)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(entry.name, color = ViewitTextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(entry.category, color = ViewitTextMuted, fontSize = 14.sp)
        }
    }
}
