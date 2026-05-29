package com.sksdesign.viewit.ui.tv

import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.focusable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import com.sksdesign.viewit.adblock.AdblockEngine
import com.sksdesign.viewit.data.model.BrowserControlSettings
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.data.model.NotificationPlayerSettings
import com.sksdesign.viewit.data.model.TvModeSettings
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.util.IntentUtils
import com.sksdesign.viewit.player.NotificationPlayerBridge
import com.sksdesign.viewit.player.NotificationPlayerController
import com.sksdesign.viewit.web.ViewitWebChromeClient
import com.sksdesign.viewit.web.ViewitWebView

@Composable
fun TvBrowserScreen(
    entry: WebAppEntry,
    themeSettings: ThemeSettings,
    tvModeSettings: TvModeSettings,
    browserSettings: BrowserControlSettings,
    notificationSettings: NotificationPlayerSettings,
    adblockEngine: AdblockEngine,
    onClose: () -> Unit,
    onMainMenu: () -> Unit
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    var chromeClient by remember { mutableStateOf<ViewitWebChromeClient?>(null) }
    var progress by remember { mutableIntStateOf(0) }
    var overlayVisible by remember { mutableStateOf(tvModeSettings.showTvControlOverlay) }
    var currentUrl by remember { mutableStateOf(entry.effectiveUrl(useTvLayout = true)) }
    var pageTitle by remember(entry.id) { mutableStateOf(entry.name) }
    val notificationController = remember(context) { NotificationPlayerController(context) }
    val rootFocusRequester = remember { FocusRequester() }

    fun handleBack() {
        when {
            chromeClient?.exitFullscreenIfNeeded() == true -> Unit
            tvModeSettings.remoteBackBehavior == "always_show_controls" -> overlayVisible = true
            webView?.canGoBack() == true && tvModeSettings.remoteBackBehavior == "webview_back_first" -> webView?.goBack()
            else -> onClose()
        }
    }

    BackHandler { handleBack() }

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

    Box(
        Modifier
            .fillMaxSize()
            .focusRequester(rootFocusRequester)
            .focusable(enabled = tvModeSettings.remoteInputEnabled)
            .onKeyEvent { event ->
                if (!tvModeSettings.remoteInputEnabled || event.type != KeyEventType.KeyUp) return@onKeyEvent false
                when (event.key) {
                    Key.Menu -> {
                        overlayVisible = !overlayVisible
                        true
                    }
                    Key.DirectionUp -> {
                        webView?.scrollBy(0, -(120 * tvModeSettings.dpadScrollSpeed).toInt())
                        true
                    }
                    Key.DirectionDown -> {
                        webView?.scrollBy(0, (120 * tvModeSettings.dpadScrollSpeed).toInt())
                        true
                    }
                    Key.DirectionLeft -> {
                        overlayVisible = true
                        true
                    }
                    Key.DirectionRight -> {
                        overlayVisible = true
                        true
                    }
                    Key.MediaPlayPause, Key.MediaStop, Key.MediaNext, Key.MediaPrevious -> tvModeSettings.enableMediaKeys
                    else -> false
                }
            }
    ) {
        ViewitWebView(
            entry = entry,
            browserSettings = browserSettings,
            adblockEngine = adblockEngine,
            useTvLayout = true,
            modifier = Modifier.fillMaxSize(),
            onWebViewReady = { view, client -> webView = view; chromeClient = client },
            onStateChanged = { state ->
                if (state.progress > 0) progress = state.progress
                if (state.currentUrl.isNotBlank()) currentUrl = state.currentUrl
                if (state.title.isNotBlank()) pageTitle = state.title
                if (notificationSettings.enabled) notificationController.show(entry, if (notificationSettings.showPageTitle) pageTitle else entry.name, currentUrl)
            }
        )
        if (progress in 1..99) LinearProgressIndicator(progress = { progress / 100f }, modifier = Modifier.align(Alignment.TopCenter))
        if (overlayVisible) {
            TvControlOverlay(
                themeSettings = themeSettings,
                modifier = Modifier.align(Alignment.BottomCenter),
                onMainMenu = onMainMenu,
                onHome = { webView?.loadUrl(entry.effectiveUrl(useTvLayout = true)) },
                onBack = { if (webView?.canGoBack() == true) webView?.goBack() },
                onForward = { if (webView?.canGoForward() == true) webView?.goForward() },
                onReload = { webView?.reload() },
                onExternal = { IntentUtils.openUrl(context, currentUrl) },
                onClose = onClose
            )
        }
    }
}
