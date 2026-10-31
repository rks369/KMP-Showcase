package org.rks369.news.bookmarks

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.rks369.news.bookmarks.db.BookmarksDatabase
import java.io.File

internal fun createBookmarkSqlDriver(): SqlDriver {
    val appDir = File(System.getProperty("user.home"), ".newsapp").apply { mkdirs() }
    val dbFile = File(appDir, "bookmarks.db")
    val needsCreate = !dbFile.exists()
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    if (needsCreate) {
        BookmarksDatabase.Schema.create(driver)
    }
    return driver
}
