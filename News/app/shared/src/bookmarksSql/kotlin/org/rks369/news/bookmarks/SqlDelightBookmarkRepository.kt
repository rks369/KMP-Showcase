package org.rks369.news.bookmarks

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.rks369.news.bookmarks.db.BookmarkEntity
import org.rks369.news.bookmarks.db.BookmarksDatabase

actual class BookmarkRepository actual constructor() {
    private val queries = BookmarksDatabase(createBookmarkSqlDriver()).bookmarksQueries

    actual fun observeBookmarks(): Flow<List<Bookmark>> =
        queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.toBookmark() } }

    actual fun observeIsBookmarked(url: String): Flow<Boolean> =
        queries.selectByUrl(url)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it != null }

    actual suspend fun toggleBookmark(bookmark: Bookmark) {
        val existing = queries.selectByUrl(bookmark.url).executeAsOneOrNull()
        if (existing != null) {
            queries.deleteByUrl(bookmark.url)
        } else {
            queries.insertOrReplace(
                url = bookmark.url,
                title = bookmark.title,
                summary = bookmark.abstract,
                thumbnailUrl = bookmark.thumbnailUrl,
                section = bookmark.section,
                publishedDate = bookmark.publishedDate,
            )
        }
    }
}

private fun BookmarkEntity.toBookmark() = Bookmark(
    url = url,
    title = title,
    abstract = summary,
    thumbnailUrl = thumbnailUrl,
    section = section,
    publishedDate = publishedDate,
)
