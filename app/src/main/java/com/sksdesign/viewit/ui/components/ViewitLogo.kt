package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.R

@Composable
fun ViewitLogo(size: Dp = 42.dp, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    val clickableModifier = if (onClick != null) modifier.clickable { onClick() } else modifier
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "VIEWit logo",
        modifier = clickableModifier.size(size)
    )
}
