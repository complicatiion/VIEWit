package com.sksdesign.viewit.web

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView

class ViewitWebChromeClient(
    private val activity: Activity?,
    private val onProgressChanged: (Int) -> Unit,
    private val onTitleChanged: (String) -> Unit
) : WebChromeClient() {
    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var originalOrientation: Int? = null

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        onProgressChanged(newProgress)
        super.onProgressChanged(view, newProgress)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        if (!title.isNullOrBlank()) onTitleChanged(title)
        super.onReceivedTitle(view, title)
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        request?.deny()
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        val currentActivity = activity ?: return
        val fullscreenView = view ?: return
        if (customView != null) {
            callback?.onCustomViewHidden()
            return
        }
        customView = fullscreenView
        customViewCallback = callback
        originalOrientation = currentActivity.requestedOrientation
        currentActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        currentActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val decor = currentActivity.window.decorView as ViewGroup
        decor.addView(fullscreenView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    override fun onHideCustomView() {
        val currentActivity = activity ?: return
        val decor = currentActivity.window.decorView as ViewGroup
        customView?.let { decor.removeView(it) }
        customView = null
        customViewCallback?.onCustomViewHidden()
        customViewCallback = null
        originalOrientation?.let { currentActivity.requestedOrientation = it }
        currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun exitFullscreenIfNeeded(): Boolean {
        if (customView == null) return false
        onHideCustomView()
        return true
    }
}
