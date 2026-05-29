package com.sksdesign.viewit.ui.tv

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary
import com.sksdesign.viewit.util.ColorUtils

@Composable
fun TvFocusGlow(
    themeSettings: ThemeSettings,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(22.dp),
    content: @Composable (Boolean) -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    val accent = ColorUtils.parseColor(themeSettings.accentColorHex)
    Card(
        modifier = modifier.onFocusChanged { focused = it.isFocused || it.hasFocus }.focusable(),
        shape = RoundedCornerShape((themeSettings.cardRadius + 8f).dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = if (focused) 0.22f else 0.10f), contentColor = ViewitTextPrimary),
        border = BorderStroke(if (focused) 3.dp else 1.dp, accent.copy(alpha = if (focused) 1f else 0.35f))
    ) {
        Box(Modifier.padding(contentPadding)) { content(focused) }
    }
}
