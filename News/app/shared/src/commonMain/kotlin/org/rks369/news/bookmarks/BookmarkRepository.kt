package org.rks369.news.bookmarks

import kotlinx.coroutines.flow.Flow

expect class BookmarkRepository() {
    fun observeBookmarks(): Flow<List<Bookmark>>
    fun observeIsBookmarked(url: String): Flow<Boolean>
    suspend fun toggleBookmark(bookmark: Bookmark)
}
