package org.rks369.news.home.data

data class Section(
    val key: String?,
    val title: String?,
    val description: String? = null,
    var isActive: Boolean = false
)