package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.util.ColorUtils

@Composable
fun GlowButton(
    label: String,
    themeSettings: ThemeSettings,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    compact: Boolean = false,
    onClick: () -> Unit
) {
    val accent = ColorUtils.parseColor(themeSettings.accentColorHex)
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = accent.copy(alpha = 0.14f), contentColor = Color.White),
        border = BorderStroke(1.dp, accent.copy(alpha = themeSettings.glowIntensity.coerceIn(0.1f, 1f))),
        contentPadding = PaddingValues(horizontal = if (compact) 12.dp else 14.dp, vertical = if (compact) 8.dp else 10.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(if (compact) 17.dp else 18.dp))
            androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
        }
        Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
