package com.sksdesign.viewit.ui.screens

import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.adblock.AdblockEngine
import com.sksdesign.viewit.data.model.BrowserControlSettings
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.NotificationPlayerSettings
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.ui.components.CyberBackground
import com.sksdesign.viewit.ui.components.GlassCard
import com.sksdesign.viewit.ui.components.GlowButton
import com.sksdesign.viewit.ui.components.ViewitLogo
import com.sksdesign.viewit.player.NotificationPlayerBridge
import com.sksdesign.viewit.player.NotificationPlayerController
import com.sksdesign.viewit.ui.theme.ViewitError
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary
import com.sksdesign.viewit.util.IntentUtils
import com.sksdesign.viewit.web.ViewitWebChromeClient
import com.sksdesign.viewit.web.ViewitWebView
import com.sksdesign.viewit.web.ViewitWebViewClient

@Composable
fun BrowserScreen(
    entry: WebAppEntry,
    themeSettings: ThemeSettings,
    browserSettings: BrowserControlSettings,
    notificationSettings: NotificationPlayerSettings,
    adblockEngine: AdblockEngine,
    onClose: () -> Unit,
    onMainMenu: () -> Unit,
    onUpdateEntry: (WebAppEntry) -> Unit
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    var chromeClient by remember { mutableStateOf<ViewitWebChromeClient?>(null) }
    var progress by remember { mutableIntStateOf(0) }
    var error by remember { mutableStateOf("") }
    var currentEntry by remember(entry.id) { mutableStateOf(entry) }
    var currentUrl by remember { mutableStateOf(entry.effectiveUrl(useTvLayout = false)) }
    var pageTitle by remember(entry.id) { mutableStateOf(entry.name) }
    val notificationController = remember(context) { NotificationPlayerController(context) }

    BackHandler {
        when {
            chromeClient?.exitFullscreenIfNeeded() == true -> Unit
            webView?.canGoBack() == true -> webView?.goBack()
            else -> onClose()
        }
    }

    DisposableEffect(webView, notificationSettings.enabled) {
        if (notificationSettings.enabled) {
            NotificationPlayerBridge.setHandler { action ->
                val view = webView ?: return@setHandler
                when (action) {
                    NotificationPlayerBridge.ACTION_PLAY_PAUSE -> view.evaluateJavascript("(function(){var m=document.querySelector('video,audio');if(m){if(m.paused){m.play();}else{m.pause();}}})()", null)
                    NotificationPlayerBridge.ACTION_PREVIOUS -> view.evaluateJavascript("(function(){var b=document.querySelector('[aria-label*=Previous i],[title*=Previous i],.previous');if(b){b.click();}})()", null)
                    NotificationPlayerBridge.ACTION_NEXT -> view.evaluateJavascript("(function(){var b=document.querySelector('[aria-label*=Next i],[title*=Next i],.next');if(b){b.click();}})()", null)
                    NotificationPlayerBridge.ACTION_STOP -> { view.evaluateJavascript("(function(){var m=document.querySelector('video,audio');if(m){m.pause();}})()", null); notificationController.cancel() }
                }
            }
        }
        onDispose {
            NotificationPlayerBridge.setHandler(null)
            notificationController.cancel()
        }
    }

    CyberBackground() {
        Column(Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                ViewitLogo(40.dp, onClick = onMainMenu)
                Text(currentEntry.name, color = ViewitTextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(0.75f).padding(start = 10.dp))
                Row(
                    modifier = Modifier.weight(1.25f).horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BrowserIconButton(Icons.Outlined.Apps, "Main menu") { onMainMenu() }
                    BrowserIconButton(Icons.Outlined.ArrowBack, "Back") { if (webView?.canGoBack() == true) webView?.goBack() }
                    BrowserIconButton(Icons.Outlined.ArrowForward, "Forward") { if (webView?.canGoForward() == true) webView?.goForward() }
                    BrowserIconButton(Icons.Outlined.Refresh, "Reload") { webView?.reload() }
                    BrowserIconButton(Icons.Outlined.Home, "Web app home") { webView?.loadUrl(currentEntry.effectiveUrl(useTvLayout = false)) }
                    BrowserIconButton(Icons.Outlined.OpenInBrowser, "Open externally") { IntentUtils.openUrl(context, currentUrl) }
                    BrowserIconButton(Icons.Outlined.Tv, if (currentEntry.desktopMode) "Use mobile view" else "Use desktop view") {
                        val updated = currentEntry.copy(desktopMode = !currentEntry.desktopMode)
                        currentEntry = updated
                        onUpdateEntry(updated)
                        webView?.let { view ->
                            view.webViewClient = ViewitWebViewClient(
                                adblockEngine = adblockEngine,
                                adblockEnabledForApp = updated.adblockEnabled,
                                preferMobileLayout = !updated.usesDesktopPresentation(useTvLayout = false),
                                onLoadingChanged = { loading -> progress = if (loading) 10 else 100 },
                                onError = { message -> error = message },
                                onUrlChanged = { url -> currentUrl = url }
                            )
                            view.loadUrl(updated.effectiveUrl(useTvLayout = false))
                        }
                    }
                    BrowserIconButton(Icons.Outlined.Shield, "Toggle adblock") {
                        val updated = currentEntry.copy(adblockEnabled = !currentEntry.adblockEnabled)
                        currentEntry = updated
                        onUpdateEntry(updated)
                        webView?.reload()
                    }
                    BrowserIconButton(Icons.Outlined.DeleteSweep, "Clear site data") {
                        webView?.clearCache(true)
                        CookieManager.getInstance().removeAllCookies(null)
                        CookieManager.getInstance().flush()
                    }
                    BrowserIconButton(Icons.Outlined.Close, "Close") { onClose() }
                }
            }
            if (progress in 1..99) {
                LinearProgressIndicator(progress = { progress / 100f }, modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 4.dp))
            } else {
                Spacer(Modifier.height(6.dp))
            }
            if (error.isNotBlank()) {
                GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Page load error", color = ViewitError)
                        Text(error, color = ViewitTextPrimary)
                        GlowButton("Retry", themeSettings, icon = Icons.Outlined.Refresh) { error = ""; webView?.reload() }
                    }
                }
                Spacer(Modifier.height(6.dp))
            }
            Box(Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(themeSettings.cardRadius.dp))) {
                ViewitWebView(
                    entry = currentEntry,
                    browserSettings = browserSettings,
                    adblockEngine = adblockEngine,
                    modifier = Modifier.fillMaxSize(),
                    onWebViewReady = { view, client -> webView = view; chromeClient = client },
                    onStateChanged = { state ->
                        if (state.progress > 0) progress = state.progress
                        if (state.currentUrl.isNotBlank()) currentUrl = state.currentUrl
                        if (state.title.isNotBlank()) pageTitle = state.title
                        if (notificationSettings.enabled) notificationController.show(currentEntry, if (notificationSettings.showPageTitle) pageTitle else currentEntry.name, currentUrl)
                        if (state.hasError) error = state.errorMessage
                    }
                )
            }
        }
    }
}

@Composable
private fun BrowserIconButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    Surface(color = Color.White.copy(alpha = 0.10f), shape = RoundedCornerShape(14.dp), modifier = Modifier.size(42.dp)) {
        IconButton(onClick = onClick) { Icon(icon, contentDescription = description, tint = ViewitTextPrimary) }
    }
}
