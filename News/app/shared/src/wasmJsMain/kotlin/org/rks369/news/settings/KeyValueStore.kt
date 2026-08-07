package org.rks369.news.settings

actual object KeyValueStore {
    private val store = mutableMapOf<String, String>()

    actual fun getString(key: String): String? = store[key]

    actual fun putString(key: String, value: String) {
        store[key] = value
    }
}