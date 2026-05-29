package com.sksdesign.viewit.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        NotificationPlayerBridge.dispatch(action)
    }
}
