package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.sksdesign.viewit.ui.theme.ViewitBackground
import com.sksdesign.viewit.ui.theme.ViewitBackgroundSoft

@Composable
fun CyberBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.05f), ViewitBackgroundSoft, ViewitBackground),
                    center = Offset(220f, 120f),
                    radius = 900f
                )
            )
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(Color.White.copy(alpha = 0.06f), radius = size.minDimension * 0.38f, center = Offset(size.width * 0.85f, size.height * 0.18f))
            drawCircle(Color.White.copy(alpha = 0.035f), radius = size.minDimension * 0.42f, center = Offset(size.width * 0.16f, size.height * 0.88f))
        }
        content()
    }
}
