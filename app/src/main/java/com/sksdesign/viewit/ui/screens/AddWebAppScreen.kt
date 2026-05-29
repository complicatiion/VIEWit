package com.sksdesign.viewit.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.ui.components.CyberBackground
import com.sksdesign.viewit.ui.components.GlassCard
import com.sksdesign.viewit.ui.components.GlowButton
import com.sksdesign.viewit.ui.components.SectionHeader
import com.sksdesign.viewit.ui.components.ViewitOutlinedTextField
import com.sksdesign.viewit.ui.components.ViewitSwitchRow
import com.sksdesign.viewit.ui.components.iconFor
import com.sksdesign.viewit.ui.theme.ViewitError
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary
import com.sksdesign.viewit.util.ColorUtils
import com.sksdesign.viewit.util.UrlUtils

@Composable
fun AddWebAppScreen(
    themeSettings: ThemeSettings,
    initialEntry: WebAppEntry?,
    onSave: (WebAppEntry) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(initialEntry?.name ?: "") }
    var url by remember { mutableStateOf(initialEntry?.url ?: "") }
    var mobileUrl by remember { mutableStateOf(initialEntry?.mobileUrl ?: "") }
    var desktopUrl by remember { mutableStateOf(initialEntry?.desktopUrl ?: "") }
    var category by remember { mutableStateOf(initialEntry?.category ?: "Web") }
    var iconStyle by remember { mutableStateOf(initialEntry?.iconStyle ?: "web") }
    var iconColor by remember { mutableStateOf(initialEntry?.iconColor ?: "#FFFFFF") }
    var userAgent by remember { mutableStateOf(initialEntry?.customUserAgent ?: "") }
    var javascript by remember { mutableStateOf(initialEntry?.javascriptEnabled ?: true) }
    var desktop by remember { mutableStateOf(initialEntry?.desktopMode ?: false) }
    var adblock by remember { mutableStateOf(initialEntry?.adblockEnabled ?: true) }
    var cookies by remember { mutableStateOf(initialEntry?.cookiesEnabled ?: true) }
    var domStorage by remember { mutableStateOf(initialEntry?.domStorageEnabled ?: true) }
    var cache by remember { mutableStateOf(initialEntry?.cacheEnabled ?: true) }
    var favorite by remember { mutableStateOf(initialEntry?.favorite ?: false) }
    var error by remember { mutableStateOf("") }

    BackHandler { onBack() }

    CyberBackground() {
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp).verticalScroll(rememberScrollState())) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionHeader(if (initialEntry == null) "Add Web App" else "Edit Web App")
                Spacer(Modifier.weight(1f))
                GlowButton("Back", themeSettings, icon = Icons.Outlined.ArrowBack, compact = true, onClick = onBack)
            }
            Spacer(Modifier.height(14.dp))
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ViewitOutlinedTextField(value = name, onValueChange = { name = it }, label = "Name", modifier = Modifier.fillMaxWidth())
                    ViewitOutlinedTextField(value = url, onValueChange = { url = it }, label = "Main URL", modifier = Modifier.fillMaxWidth())
                    ViewitOutlinedTextField(value = mobileUrl, onValueChange = { mobileUrl = it }, label = "Mobile URL", modifier = Modifier.fillMaxWidth())
                    ViewitOutlinedTextField(value = desktopUrl, onValueChange = { desktopUrl = it }, label = "TV/Desktop URL", modifier = Modifier.fillMaxWidth())
                    ViewitOutlinedTextField(value = category, onValueChange = { category = it }, label = "Category", modifier = Modifier.fillMaxWidth())
                    IconSelector(selected = iconStyle, themeSettings = themeSettings) { iconStyle = it }
                    ViewitOutlinedTextField(value = iconColor, onValueChange = { iconColor = it }, label = "Icon Color", modifier = Modifier.fillMaxWidth())
                    ViewitOutlinedTextField(value = userAgent, onValueChange = { userAgent = it }, label = "Custom User-Agent", modifier = Modifier.fillMaxWidth())
                    ViewitSwitchRow("JavaScript", javascript) { javascript = it }
                    ViewitSwitchRow("Desktop View on Mobile", desktop) { desktop = it }
                    ViewitSwitchRow("Adblock", adblock) { adblock = it }
                    ViewitSwitchRow("Cookies", cookies) { cookies = it }
                    ViewitSwitchRow("DOM Storage", domStorage) { domStorage = it }
                    ViewitSwitchRow("Cache", cache) { cache = it }
                    ViewitSwitchRow("Favorite", favorite) { favorite = it }
                    if (UrlUtils.isHttpWarning(url)) Text("Warning: HTTP is not encrypted.", color = ViewitTextMuted)
                    if (error.isNotBlank()) Text(error, color = ViewitError)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        GlowButton("Cancel", themeSettings, icon = Icons.Outlined.ArrowBack, modifier = Modifier.weight(1f), onClick = onBack)
                        GlowButton("Save", themeSettings, icon = Icons.Outlined.Save, modifier = Modifier.weight(1f)) {
                            val normalized = UrlUtils.validateHttpUrl(url)
                            val normalizedMobile = if (mobileUrl.isBlank()) normalized else UrlUtils.validateHttpUrl(mobileUrl)
                            val normalizedDesktop = if (desktopUrl.isBlank()) normalized else UrlUtils.validateHttpUrl(desktopUrl)
                            if (name.isBlank()) {
                                error = "Name must not be empty."
                            } else if (normalized.isFailure) {
                                error = normalized.exceptionOrNull()?.message ?: "The URL is not valid."
                            } else if (normalizedMobile.isFailure) {
                                error = normalizedMobile.exceptionOrNull()?.message ?: "The mobile URL is not valid."
                            } else if (normalizedDesktop.isFailure) {
                                error = normalizedDesktop.exceptionOrNull()?.message ?: "The desktop URL is not valid."
                            } else {
                                onSave((initialEntry ?: WebAppEntry(name = name, url = normalized.getOrThrow())).copy(
                                    name = name.trim(),
                                    url = normalized.getOrThrow(),
                                    mobileUrl = normalizedMobile.getOrThrow(),
                                    desktopUrl = normalizedDesktop.getOrThrow(),
                                    category = category.trim().ifBlank { "Web" },
                                    iconStyle = iconStyle.trim().ifBlank { "web" },
                                    iconColor = ColorUtils.normalizeHex(iconColor),
                                    customUserAgent = userAgent.trim(),
                                    javascriptEnabled = javascript,
                                    desktopMode = desktop,
                                    adblockEnabled = adblock,
                                    cookiesEnabled = cookies,
                                    domStorageEnabled = domStorage,
                                    cacheEnabled = cache,
                                    favorite = favorite
                                ))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IconSelector(selected: String, themeSettings: ThemeSettings, onSelected: (String) -> Unit) {
    val options = listOf("web", "globe", "music", "player", "video", "radio", "news", "game", "bookmark", "dashboard", "cloud", "lock")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Icon Style", color = ViewitTextPrimary)
        options.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { style ->
                    IconChoice(style, selected == style, themeSettings, Modifier.weight(1f)) { onSelected(style) }
                }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun IconChoice(style: String, selected: Boolean, themeSettings: ThemeSettings, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(themeSettings.cardRadius.dp),
        color = Color.White.copy(alpha = if (selected) 0.18f else 0.07f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = if (selected) 0.85f else 0.18f))
    ) {
        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Image(painter = painterResource(iconFor(style)), contentDescription = style, colorFilter = ColorFilter.tint(ViewitTextPrimary), modifier = Modifier.size(22.dp))
            Text(style, color = ViewitTextPrimary, maxLines = 1)
        }
    }
}
