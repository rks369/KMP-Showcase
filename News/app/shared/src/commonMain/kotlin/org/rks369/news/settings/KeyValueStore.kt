package org.rks369.news.settings

expect object KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
}