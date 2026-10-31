package org.rks369.news.saved

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.rks369.news.home.ArticleListItem

@Composable
fun SavedView(
    savedViewModel: SavedViewModel = viewModel { SavedViewModel() },
) {
    val bookmarks by savedViewModel.bookmarks.collectAsState()

    if (bookmarks.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "No saved articles")
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 320.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(bookmarks, key = { it.url }) { bookmark ->
                ArticleListItem(
                    title = bookmark.title,
                    abstract = bookmark.abstract,
                    thumbnailUrl = bookmark.thumbnailUrl,
                    section = bookmark.section,
                    publishedDate = bookmark.publishedDate,
                    isBookmarked = true,
                    onToggleBookmark = { savedViewModel.removeBookmark(bookmark) }
                )
            }
        }
    }
}
