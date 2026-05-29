package com.sksdesign.viewit.web

import android.app.Activity
import android.graphics.Color as AndroidColor
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.sksdesign.viewit.adblock.AdblockEngine
import com.sksdesign.viewit.data.model.BrowserControlSettings
import com.sksdesign.viewit.data.model.WebAppEntry

@Composable
fun ViewitWebView(
    entry: WebAppEntry,
    browserSettings: BrowserControlSettings,
    adblockEngine: AdblockEngine,
    useTvLayout: Boolean = false,
    modifier: Modifier = Modifier,
    onWebViewReady: (WebView, ViewitWebChromeClient) -> Unit,
    onStateChanged: (WebViewState) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val webChromeClient = remember {
        ViewitWebChromeClient(
            activity = activity,
            onProgressChanged = { progress -> onStateChanged(WebViewState(progress = progress)) },
            onTitleChanged = { title -> onStateChanged(WebViewState(title = title)) }
        )
    }

    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                configureWebView(this, entry, browserSettings, useTvLayout)
                webViewClient = ViewitWebViewClient(
                    adblockEngine = adblockEngine,
                    adblockEnabledForApp = entry.adblockEnabled,
                    preferMobileLayout = !entry.usesDesktopPresentation(useTvLayout),
                    forceNoHorizontalOverflow = useTvLayout,
                    viewportFixMode = tvViewportFixMode(entry),
                    onLoadingChanged = { loading -> onStateChanged(WebViewState(progress = if (loading) 10 else 100)) },
                    onError = { message -> onStateChanged(WebViewState(hasError = true, errorMessage = message)) },
                    onUrlChanged = { url -> onStateChanged(WebViewState(currentUrl = url, canGoBack = canGoBack(), canGoForward = canGoForward())) }
                )
                setWebChromeClient(webChromeClient)
                setDownloadListener(DownloadHandler(context))
                loadUrl(entry.effectiveUrl(useTvLayout))
                onWebViewReady(this, webChromeClient)
            }
        },
        update = { webView ->
            configureWebView(webView, entry, browserSettings, useTvLayout)
            webView.webViewClient = ViewitWebViewClient(
                adblockEngine = adblockEngine,
                adblockEnabledForApp = entry.adblockEnabled,
                preferMobileLayout = !entry.usesDesktopPresentation(useTvLayout),
                forceNoHorizontalOverflow = useTvLayout,
                viewportFixMode = tvViewportFixMode(entry),
                onLoadingChanged = { loading -> onStateChanged(WebViewState(progress = if (loading) 10 else 100)) },
                onError = { message -> onStateChanged(WebViewState(hasError = true, errorMessage = message)) },
                onUrlChanged = { url -> onStateChanged(WebViewState(currentUrl = url, canGoBack = webView.canGoBack(), canGoForward = webView.canGoForward())) }
            )
            onWebViewReady(webView, webChromeClient)
        }
    )

    DisposableEffect(Unit) {
        onDispose { }
    }
}

private fun tvViewportFixMode(entry: WebAppEntry): String {
    val key = "${entry.id} ${entry.name} ${entry.url} ${entry.desktopUrl}".lowercase()
    return if ("soundcloud" in key || "spotify" in key) "tv_service_fit" else "generic"
}

private fun configureWebView(webView: WebView, entry: WebAppEntry, browserSettings: BrowserControlSettings, useTvLayout: Boolean) {
    val settings = webView.settings
    val desktopPresentation = entry.usesDesktopPresentation(useTvLayout)
    settings.javaScriptEnabled = entry.javascriptEnabled
    settings.domStorageEnabled = entry.domStorageEnabled
    settings.databaseEnabled = entry.domStorageEnabled
    settings.mediaPlaybackRequiresUserGesture = !browserSettings.mediaPlaybackWithoutGesture
    settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
    settings.cacheMode = when {
        !entry.cacheEnabled -> WebSettings.LOAD_NO_CACHE
        browserSettings.cacheMode == "no_cache" -> WebSettings.LOAD_NO_CACHE
        browserSettings.cacheMode == "cache_else_network" -> WebSettings.LOAD_CACHE_ELSE_NETWORK
        else -> WebSettings.LOAD_DEFAULT
    }
    settings.userAgentString = UserAgentManager.resolve(WebSettings.getDefaultUserAgent(webView.context), desktopPresentation, entry.customUserAgent)
    settings.setSupportZoom(true)
    settings.useWideViewPort = desktopPresentation
    settings.loadWithOverviewMode = desktopPresentation
    if (useTvLayout && tvViewportFixMode(entry) == "tv_service_fit") {
        webView.setInitialScale(100)
    } else {
        webView.setInitialScale(0)
    }
    settings.builtInZoomControls = false
    settings.displayZoomControls = false
    webView.setPadding(0, 0, 0, 0)
    webView.setBackgroundColor(AndroidColor.BLACK)
    webView.isHorizontalScrollBarEnabled = false
    webView.isVerticalScrollBarEnabled = true
    webView.isScrollbarFadingEnabled = true
    webView.overScrollMode = WebView.OVER_SCROLL_NEVER
    CookieManager.getInstance().setAcceptCookie(entry.cookiesEnabled)
    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, browserSettings.thirdPartyCookies)
    WebView.setWebContentsDebuggingEnabled(false)
}
