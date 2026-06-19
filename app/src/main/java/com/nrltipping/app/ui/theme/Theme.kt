package com.nrltipping.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NrlBlue = Color(0xFF0B3D91)
private val NrlGold = Color(0xFFFFD400)

private val LightColors = lightColorScheme(
    primary = NrlBlue,
    secondary = NrlGold,
)

private val DarkColors = darkColorScheme(
    primary = NrlGold,
    secondary = NrlBlue,
)

@Composable
fun NrlTippingTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
