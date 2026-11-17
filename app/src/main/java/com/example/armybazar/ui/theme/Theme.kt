package com.example.armybazar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = Blue,
    tertiary = BackgroundMain,
    background = BackgroundMain,
    surface = SurfaceLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueDark,
    secondary = BlueDark,
    tertiary = BackgroundMainDark,
    background = BackgroundMainDark,
    surface = SurfaceDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

@Composable
fun MainAppThem(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val color = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = color,
        typography = Typography,
        content = content,
    )

    val systemuicontroller = rememberSystemUiController()
    systemuicontroller.setSystemBarsColor(
        if (darkTheme) BackgroundMainDark else Blue
    )
}