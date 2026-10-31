package org.rks369.news.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.rks369.news.asyncSnapshotBuilder.AsyncSnapshot
import org.rks369.news.asyncSnapshotBuilder.load
import org.rks369.news.bookmarks.BookmarkRepository
import org.rks369.news.bookmarks.BookmarksStore
import org.rks369.news.bookmarks.toBookmark
import org.rks369.news.home.data.Article
import org.rks369.news.home.data.NewsRepository
import org.rks369.news.home.data.Section

class HomeViewModel(
    private val newsRepository: NewsRepository = NewsRepository(),
    private val bookmarkRepository: BookmarkRepository = BookmarksStore.repository,
) : ViewModel() {
    val heading : String = "Top Stories"

    val sectionsAvailable: List<Section> = newsRepository.sections()
    private val _selectedSection = MutableStateFlow<Section?>(null)
    val selectedSection: StateFlow<Section?> = _selectedSection.asStateFlow()

    val bookmarkedUrls: StateFlow<Set<String>> = bookmarkRepository.observeBookmarks()
        .map { bookmarks -> bookmarks.map { it.url }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun updateSelectedSection(section: Section?) {
        _selectedSection.value = section
        loadArticles(sectionKey = section?.key)
    }

    private val _articlesSnapshot: MutableStateFlow<AsyncSnapshot<List<Article>>> = MutableStateFlow(AsyncSnapshot.Idle)
    val articlesSnapshot: StateFlow<AsyncSnapshot<List<Article>>> = _articlesSnapshot.asStateFlow()

    private fun loadArticles(
        sectionKey: String?,
    ) {
        viewModelScope.launch {
            _articlesSnapshot.load {
                newsRepository.getArticles(
                    sectionKey = sectionKey
                )
            }
        }
    }

    fun retry() {
        loadArticles(
            sectionKey = _selectedSection.value?.key
        )
    }

    fun toggleBookmark(article: Article) {
        val bookmark = article.toBookmark() ?: return
        viewModelScope.launch { bookmarkRepository.toggleBookmark(bookmark) }
    }

}