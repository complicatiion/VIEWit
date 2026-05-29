package com.sksdesign.viewit.input

import android.view.KeyEvent

object MediaKeyHandler {
    fun isMediaKey(keyCode: Int): Boolean = keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ||
        keyCode == KeyEvent.KEYCODE_MEDIA_PLAY ||
        keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE ||
        keyCode == KeyEvent.KEYCODE_MEDIA_STOP ||
        keyCode == KeyEvent.KEYCODE_MEDIA_NEXT ||
        keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS
}
