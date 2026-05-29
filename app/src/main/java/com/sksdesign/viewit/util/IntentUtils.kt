package com.sksdesign.viewit.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

object IntentUtils {
    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { ContextCompat.startActivity(context, intent, null) }
    }

    fun openPrivateDnsSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { ContextCompat.startActivity(context, intent, null) }
    }
}
