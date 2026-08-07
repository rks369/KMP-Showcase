package org.rks369.news

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory

import org.rks369.news.home.HomeView
import org.rks369.news.settings.ThemePreferences
import org.rks369.news.theme.AppTheme

@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    var themeMode by remember { mutableStateOf(ThemePreferences.getThemeMode()) }

    AppTheme(themeMode = themeMode) {
        HomeView(
            themeMode = themeMode,
            onThemeModeChange = { mode ->
                themeMode = mode
                ThemePreferences.setThemeMode(mode)
            }
        )
    }
}