package org.rks369.news.bookmarks

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.rks369.news.bookmarks.db.BookmarksDatabase
import org.rks369.news.platform.AndroidAppContext

internal fun createBookmarkSqlDriver(): SqlDriver =
    AndroidSqliteDriver(
        schema = BookmarksDatabase.Schema,
        context = AndroidAppContext.context,
        name = "bookmarks.db"
    )
