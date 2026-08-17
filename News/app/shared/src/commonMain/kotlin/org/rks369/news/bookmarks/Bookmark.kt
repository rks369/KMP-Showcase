package org.rks369.news.bookmarks

import org.rks369.news.home.data.Article

data class Bookmark(
    val url: String,
    val title: String,
    val abstract: String?,
    val thumbnailUrl: String?,
    val section: String?,
    val publishedDate: String?,
)

fun Article.toBookmark(): Bookmark? {
    val articleUrl = url ?: return null
    return Bookmark(
        url = articleUrl,
        title = title,
        abstract = abstract,
        thumbnailUrl = thumbnailUrl,
        section = section,
        publishedDate = publishedDate,
    )
}
