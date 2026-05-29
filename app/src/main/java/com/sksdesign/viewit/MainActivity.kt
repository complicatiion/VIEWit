package com.sksdesign.viewit

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.sksdesign.viewit.ui.theme.ViewitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureWindow(window)
        setContent {
            ViewitTheme {
                ViewitApp()
            }
        }
    }

    private fun configureWindow(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = android.graphics.Color.rgb(5, 5, 5)
        window.navigationBarColor = android.graphics.Color.rgb(5, 5, 5)
    }
}
