package org.rks369.news.settings

private const val KEY_THEME_MODE = "theme_mode"

object ThemePreferences {
    fun getThemeMode(): ThemeMode {
        val stored = KeyValueStore.getString(KEY_THEME_MODE) ?: return ThemeMode.SYSTEM
        return runCatching { ThemeMode.valueOf(stored) }.getOrDefault(ThemeMode.SYSTEM)
    }

    fun setThemeMode(mode: ThemeMode) {
        KeyValueStore.putString(KEY_THEME_MODE, mode.name)
    }
}