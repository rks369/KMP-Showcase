package org.rks369.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory

import org.rks369.news.home.HomeView
import org.rks369.news.saved.SavedView
import org.rks369.news.settings.ThemePreferences
import org.rks369.news.theme.AppTheme

private enum class AppDestination(val label: String) {
    Home("Top Stories"),
    Saved("Saved");

    fun icon(selected: Boolean): ImageVector = when (this) {
        Home -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
        Saved -> if (selected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
    }
}

// Material's compact/medium width class breakpoint: below this the nav lives
// in a bottom bar, at or above it the nav shifts to a rail on the left.
private val WideScreenBreakpoint = 600.dp

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
    var destination by remember { mutableStateOf(AppDestination.Home) }

    AppTheme(themeMode = themeMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isWideScreen = maxWidth >= WideScreenBreakpoint

                val content: @Composable () -> Unit = {
                    when (destination) {
                        AppDestination.Home -> HomeView(
                            themeMode = themeMode,
                            onThemeModeChange = { mode ->
                                themeMode = mode
                                ThemePreferences.setThemeMode(mode)
                            }
                        )
                        AppDestination.Saved -> SavedView()
                    }
                }

                if (isWideScreen) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        NavigationRail {
                            AppDestination.entries.forEach { dest ->
                                NavigationRailItem(
                                    selected = destination == dest,
                                    onClick = { destination = dest },
                                    icon = {
                                        Icon(
                                            imageVector = dest.icon(destination == dest),
                                            contentDescription = dest.label
                                        )
                                    },
                                    label = { Text(dest.label) }
                                )
                            }
                        }
                        Box(modifier = Modifier.weight(1f)) { content() }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.weight(1f)) { content() }
                        NavigationBar {
                            AppDestination.entries.forEach { dest ->
                                NavigationBarItem(
                                    selected = destination == dest,
                                    onClick = { destination = dest },
                                    icon = {
                                        Icon(
                                            imageVector = dest.icon(destination == dest),
                                            contentDescription = dest.label
                                        )
                                    },
                                    label = { Text(dest.label) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
