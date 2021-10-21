package com.alex.mediacenter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

val DarkColorPalette = darkColors(
    primary = Teal,
    primaryVariant = AquaDeep,
    secondary = Amaranth
)

val LightColorPalette = lightColors(
    primary = Teal,
    primaryVariant = AquaDeep,
    secondary = Amaranth
)

@Composable
fun MediaCenterTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content)
}