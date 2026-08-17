package org.rks369.news.bookmarks

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.rks369.news.bookmarks.db.BookmarksDatabase

internal fun createBookmarkSqlDriver(): SqlDriver =
    NativeSqliteDriver(BookmarksDatabase.Schema, "bookmarks.db")
