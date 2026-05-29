package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.ui.theme.ViewitGlass
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary

@Composable
fun GlassCard(
    themeSettings: ThemeSettings,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(themeSettings.cardRadius.dp)
    Card(
        modifier = modifier.shadow(14.dp, shape, clip = false),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = themeSettings.glassIntensity.coerceIn(0.04f, 0.30f)),
            contentColor = ViewitTextPrimary
        ),
        border = BorderStroke(1.dp, ViewitGlass.copy(alpha = 0.55f))
    ) {
        Box(Modifier.background(Color.White.copy(alpha = 0.02f)).padding(contentPadding)) {
            content()
        }
    }
}
