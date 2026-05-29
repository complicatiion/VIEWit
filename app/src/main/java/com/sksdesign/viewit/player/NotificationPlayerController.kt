package com.sksdesign.viewit.player

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.sksdesign.viewit.R
import com.sksdesign.viewit.data.model.WebAppEntry

class NotificationPlayerController(private val context: Context) {
    private val appContext = context.applicationContext

    fun show(entry: WebAppEntry, title: String, currentUrl: String) {
        if (!ensurePermission()) return
        createChannel()
        val safeTitle = title.ifBlank { entry.name }
        val safeUrl = currentUrl.ifBlank { entry.effectiveUrl(false) }
        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_viewit_logo)
            .setContentTitle(safeTitle)
            .setContentText("Playing in ${entry.name}")
            .setSubText(safeUrl)
            .setOnlyAlertOnce(true)
            .setOngoing(false)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(R.drawable.ic_player, "Previous", actionIntent(NotificationPlayerBridge.ACTION_PREVIOUS, 1))
            .addAction(R.drawable.ic_player, "Play/Pause", actionIntent(NotificationPlayerBridge.ACTION_PLAY_PAUSE, 2))
            .addAction(R.drawable.ic_player, "Next", actionIntent(NotificationPlayerBridge.ACTION_NEXT, 3))
            .addAction(R.drawable.ic_close_custom, "Stop", actionIntent(NotificationPlayerBridge.ACTION_STOP, 4))
            .build()
        NotificationManagerCompat.from(appContext).notify(NOTIFICATION_ID, notification)
    }

    fun cancel() {
        NotificationManagerCompat.from(appContext).cancel(NOTIFICATION_ID)
    }

    private fun actionIntent(action: String, requestCode: Int): PendingIntent {
        val intent = Intent(appContext, NotificationActionReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(
            appContext,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun ensurePermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        val granted = ContextCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        if (granted) return true
        val activity = context as? Activity ?: return false
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATIONS)
        return false
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(CHANNEL_ID, "VIEWit Player", NotificationManager.IMPORTANCE_LOW).apply {
            description = "Media controls for VIEWit WebView playback."
            setShowBadge(false)
        }
        appContext.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "viewit_player"
        private const val NOTIFICATION_ID = 2205
        private const val REQUEST_CODE_NOTIFICATIONS = 6710
    }
}
