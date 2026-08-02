package org.rks369.news

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import org.rks369.news.home.HomeView

@Composable
@Preview
fun App() {
    MaterialTheme {
        HomeView()
    }
}