package com.example.skillshare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController


val LightBlue = Color(0xFFADD8E6)
val LightPurple = Color(0xFFD0B3FF)
val DarkPurple = Color(0xFF6A1B9A)
val Cream = Color(0xFFFFFBEA)
val White = Color(0xFFFFFFFF)
val MediumPurple = androidx.compose.ui.graphics.Color(0xFF5D3FD3)


private val LightColors = lightColorScheme(
    primary = MediumPurple,
    onPrimary = White,
    secondary = LightBlue,
    onSecondary = DarkPurple,
    background = Cream,
    onBackground = DarkPurple,
    surface = White,
    onSurface = DarkPurple
)


private val DarkColors = darkColorScheme(
    primary = MediumPurple,
    onPrimary = White,
    secondary = DarkPurple,
    onSecondary = DarkPurple,
    background = Color(0xFF121212),
    onBackground = LightBlue,
    surface = Color(0xFF1E1E1E),
    onSurface = Cream
)

@Composable
fun SkillShareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),

    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme


    SideEffect {
        systemUiController.setStatusBarColor(
            color = DarkPurple,
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = colorScheme.background,
            darkIcons = useDarkIcons
        )
    }

    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}


annotation class SkillshareTheme
