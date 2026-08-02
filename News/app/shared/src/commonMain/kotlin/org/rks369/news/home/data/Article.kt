package org.rks369.news.home.data

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val title: String,
    val abstract: String? = null,
    val url: String? = null
)

@Serializable
data class TopStoriesResponse(
    val status: String? = null,
    val results: List<Article> = emptyList()
)
