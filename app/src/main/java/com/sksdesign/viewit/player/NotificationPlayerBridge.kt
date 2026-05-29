package com.sksdesign.viewit.player

object NotificationPlayerBridge {
    const val ACTION_PLAY_PAUSE = "com.sksdesign.viewit.action.PLAY_PAUSE"
    const val ACTION_PREVIOUS = "com.sksdesign.viewit.action.PREVIOUS"
    const val ACTION_NEXT = "com.sksdesign.viewit.action.NEXT"
    const val ACTION_STOP = "com.sksdesign.viewit.action.STOP"

    private var handler: ((String) -> Unit)? = null

    fun setHandler(callback: ((String) -> Unit)?) {
        handler = callback
    }

    fun dispatch(action: String) {
        handler?.invoke(action)
    }
}
