package com.sksdesign.viewit.input

import android.view.KeyEvent

object RemoteKeyHandler {
    fun isConfirm(keyCode: Int): Boolean = keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER
    fun isDirectional(keyCode: Int): Boolean = keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
    fun isMenu(keyCode: Int): Boolean = keyCode == KeyEvent.KEYCODE_MENU
    fun isBack(keyCode: Int): Boolean = keyCode == KeyEvent.KEYCODE_BACK
}
