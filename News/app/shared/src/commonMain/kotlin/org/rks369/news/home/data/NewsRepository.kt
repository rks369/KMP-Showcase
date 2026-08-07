package org.rks369.news.home.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.rks369.news.secrets.Secrets


class NewsRepository(
    private val httpClient: HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    },
) {
    fun sections(): List<Section> = listOf(
        Section(key = "arts", title = "Arts"),
        Section(key = "automobiles", title = "Automobiles"),
        Section(key = "books/review", title = "Books/Review"),
        Section(key = "business", title = "Business"),
        Section(key = "fashion", title = "Fashion"),
        Section(key = "food", title = "Food"),
        Section(key = "health", title = "Health"),
        Section(key = "home", title = "Home"),
        Section(key = "insider", title = "Insider"),
        Section(key = "magazine", title = "Magazine"),
        Section(key = "movies", title = "Movies"),
        Section(key = "nyregion", title = "New York Region"),
        Section(key = "obituaries", title = "Obituaries"),
        Section(key = "opinion", title = "Opinion"),
        Section(key = "politics", title = "Politics"),
        Section(key = "realestate", title = "Realestate"),
        Section(key = "science", title = "Science"),
        Section(key = "sports", title = "Sports"),
        Section(key = "sundayreview", title = "Sunday Review"),
        Section(key = "technology", title = "Technology"),
        Section(key = "theater", title = "Theater"),
        Section(key = "t-magazine", title = "T Magazine"),
        Section(key = "travel", title = "Travel"),
        Section(key = "pshot", title = "Upshot"),
        Section(key = "us", title = "United State"),
        Section(key = "world", title = "World"),
    )

    suspend fun getArticles(
        sectionKey: String?
    ): List<Article> {
        if (sectionKey == null) return emptyList()
        return httpClient.get("https://api.nytimes.com/svc/topstories/v2/$sectionKey.json?api-key=${Secrets.NYT_API_KEY}") {

        }.body<TopStoriesResponse>().results
    }

}