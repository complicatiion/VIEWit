package com.sksdesign.viewit.input

class DpadNavigationManager(
    var scrollSpeed: Float = 1.0f
) {
    fun scaledDelta(base: Float): Float = base * scrollSpeed.coerceIn(0.25f, 3.0f)
}
