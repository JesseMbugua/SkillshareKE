package com.example.skillshare.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// 🎨 Palette
val LightBlue = Color(0xFFADD8E6)
val LightPurple = Color(0xFFD0B3FF)
val DarkPurple = Color(0xFF6A1B9A)
val Cream = Color(0xFFFFFBEA)
val White = Color(0xFFFFFFFF)

// 🌞 Light mode
private val LightColors = lightColorScheme(
    primary = LightPurple,
    onPrimary = White,
    secondary = LightBlue,
    onSecondary = DarkPurple,
    background = Cream,
    onBackground = DarkPurple,
    surface = White,
    onSurface = DarkPurple
)

// 🌙 Dark mode
private val DarkColors = darkColorScheme(
    primary = DarkPurple,
    onPrimary = White,
    secondary = LightPurple,
    onSecondary = DarkPurple,
    background = Color(0xFF121212),
    onBackground = LightBlue,
    surface = Color(0xFF1E1E1E),
    onSurface = Cream
)

@Composable
fun SkillShareTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme

    // ✅ Apply light purple status bar after composition
    SideEffect {
        systemUiController.setStatusBarColor(
            color = LightPurple,
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = colorScheme.background,
            darkIcons = useDarkIcons
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
