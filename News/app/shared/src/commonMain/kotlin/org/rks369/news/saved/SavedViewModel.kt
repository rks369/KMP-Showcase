package org.rks369.news.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.rks369.news.bookmarks.Bookmark
import org.rks369.news.bookmarks.BookmarkRepository
import org.rks369.news.bookmarks.BookmarksStore

class SavedViewModel(
    private val bookmarkRepository: BookmarkRepository = BookmarksStore.repository,
) : ViewModel() {
    val bookmarks: StateFlow<List<Bookmark>> = bookmarkRepository.observeBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeBookmark(bookmark: Bookmark) {
        viewModelScope.launch { bookmarkRepository.toggleBookmark(bookmark) }
    }
}
