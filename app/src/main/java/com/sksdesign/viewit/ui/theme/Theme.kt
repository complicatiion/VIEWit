package com.sksdesign.viewit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ViewitDarkScheme = darkColorScheme(
    primary = ViewitAccent,
    secondary = ViewitAccentSoft,
    tertiary = ViewitAccentMuted,
    background = ViewitBackground,
    surface = ViewitSurface,
    surfaceVariant = ViewitSurface,
    onPrimary = ViewitBackground,
    onSecondary = ViewitTextPrimary,
    onTertiary = ViewitTextPrimary,
    onBackground = ViewitTextPrimary,
    onSurface = ViewitTextPrimary,
    onSurfaceVariant = ViewitTextPrimary,
    error = ViewitError,
    onError = ViewitTextPrimary
)

@Composable
fun ViewitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ViewitDarkScheme,
        typography = ViewitTypography,
        content = content
    )
}
