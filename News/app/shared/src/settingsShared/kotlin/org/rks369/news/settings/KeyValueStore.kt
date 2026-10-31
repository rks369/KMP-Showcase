package org.rks369.news.settings

import com.russhwolf.settings.Settings

actual object KeyValueStore {
    private val settings: Settings = Settings()

    actual fun getString(key: String): String? = settings.getStringOrNull(key)

    actual fun putString(key: String, value: String) {
        settings.putString(key, value)
    }
}