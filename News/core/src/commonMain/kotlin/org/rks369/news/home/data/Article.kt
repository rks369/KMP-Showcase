package org.rks369.news.home.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val title: String,
    val abstract: String? = null,
    val url: String? = null,
    val section: String? = null,
    @SerialName("published_date") val publishedDate: String? = null,
    val multimedia: List<Multimedia>? = null
) {
    private val preferredThumbnailFormats = listOf(
        "mediumThreeByTwo440", "Large", "mediumThreeByTwo210", "thumbLarge", "Standard Thumbnail"
    )

    val thumbnailUrl: String?
        get() = preferredThumbnailFormats.firstNotNullOfOrNull { format ->
            multimedia?.firstOrNull { it.format == format }?.url
        } ?: multimedia?.firstOrNull()?.url
}

@Serializable
data class Multimedia(
    val url: String? = null,
    val format: String? = null,
)

@Serializable
data class TopStoriesResponse(
    val status: String? = null,
    val results: List<Article> = emptyList()
)
