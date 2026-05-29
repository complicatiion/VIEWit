package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.util.ColorUtils

@Composable
fun NeonIconButton(
    icon: ImageVector,
    contentDescription: String,
    themeSettings: ThemeSettings,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val accent = ColorUtils.parseColor(themeSettings.accentColorHex)
    Surface(
        shape = CircleShape,
        color = accent.copy(alpha = 0.10f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.55f)),
        modifier = modifier.size(46.dp)
    ) {
        IconButton(onClick = onClick, colors = IconButtonDefaults.iconButtonColors(contentColor = accent)) {
            Icon(icon, contentDescription = contentDescription)
        }
    }
}
