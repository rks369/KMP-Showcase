package org.rks369.news.bookmarks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

actual class BookmarkRepository actual constructor() {
    private val bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())

    actual fun observeBookmarks(): Flow<List<Bookmark>> = bookmarks

    actual fun observeIsBookmarked(url: String): Flow<Boolean> =
        bookmarks.map { list -> list.any { it.url == url } }

    actual suspend fun toggleBookmark(bookmark: Bookmark) {
        bookmarks.value = if (bookmarks.value.any { it.url == bookmark.url }) {
            bookmarks.value.filterNot { it.url == bookmark.url }
        } else {
            listOf(bookmark) + bookmarks.value
        }
    }
}
