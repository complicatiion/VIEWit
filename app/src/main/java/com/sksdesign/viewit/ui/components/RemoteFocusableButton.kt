package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.util.ColorUtils

@Composable
fun RemoteFocusableButton(
    label: String,
    themeSettings: ThemeSettings,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    compact: Boolean = false,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    val accent = ColorUtils.parseColor(themeSettings.accentColorHex)
    Button(
        onClick = onClick,
        modifier = modifier.onFocusChanged { focused = it.isFocused }.focusable(),
        shape = RoundedCornerShape(if (compact) 14.dp else 18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = accent.copy(alpha = if (focused) 0.28f else 0.12f), contentColor = Color.White),
        border = BorderStroke(if (focused) 2.dp else 1.dp, accent.copy(alpha = if (focused) 1f else 0.45f)),
        contentPadding = PaddingValues(horizontal = if (compact) 10.dp else 14.dp, vertical = if (compact) 7.dp else 10.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(if (compact) 16.dp else 18.dp))
            androidx.compose.foundation.layout.Spacer(Modifier.size(if (compact) 6.dp else 8.dp))
        }
        Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
