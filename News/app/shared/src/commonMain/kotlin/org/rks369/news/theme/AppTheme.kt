package org.rks369.news.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.rks369.news.settings.ThemeMode

private val SeedColor = Color.Black

private val AppLightColorScheme = lightColorScheme(
    primary = SeedColor,
    onPrimary = Color.White,
    secondary = Color(0xFF44474A),
    onSecondary = Color.White,
    background = Color(0xFFFDFDFD),
    onBackground = SeedColor,
    surface = Color(0xFFFDFDFD),
    onSurface = SeedColor,
    surfaceVariant = Color(0xFFE1E2E4),
    onSurfaceVariant = Color(0xFF44474A),
    outline = Color(0xFF757779),
)

private val AppDarkColorScheme = darkColorScheme(
    primary = Color(0xFFC5C6C8),
    onPrimary = Color.Black,
    secondary = Color(0xFFC5C6C8),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE3E2E2),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE3E2E2),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFC5C6C8),
    outline = Color(0xFF8E9092),
)

@Composable
fun AppTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit,
) {
    val isDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    MaterialTheme(
        colorScheme = if (isDark) AppDarkColorScheme else AppLightColorScheme,
        content = content
    )
}