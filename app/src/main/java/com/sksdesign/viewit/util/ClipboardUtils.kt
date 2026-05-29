package com.sksdesign.viewit.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object ClipboardUtils {
    fun copy(context: Context, label: String, text: String) {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.setPrimaryClip(ClipData.newPlainText(label, text))
    }
}
