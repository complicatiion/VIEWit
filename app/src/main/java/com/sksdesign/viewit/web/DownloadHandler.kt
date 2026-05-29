package com.sksdesign.viewit.web

import android.content.Context
import android.webkit.DownloadListener
import com.sksdesign.viewit.util.IntentUtils

class DownloadHandler(private val context: Context) : DownloadListener {
    override fun onDownloadStart(url: String?, userAgent: String?, contentDisposition: String?, mimetype: String?, contentLength: Long) {
        if (!url.isNullOrBlank()) IntentUtils.openUrl(context, url)
    }
}
