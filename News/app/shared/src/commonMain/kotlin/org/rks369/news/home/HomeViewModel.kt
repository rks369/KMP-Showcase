package org.rks369.news.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.rks369.news.asyncSnapshotBuilder.AsyncSnapshot
import org.rks369.news.asyncSnapshotBuilder.load
import org.rks369.news.home.data.Article
import org.rks369.news.home.data.NewsRepository
import org.rks369.news.home.data.Section

class HomeViewModel(
    private val newsRepository: NewsRepository = NewsRepository()
) : ViewModel() {
    val heading : String = "News"

    val sectionsAvailable: List<Section> = newsRepository.sections()
    private val _selectedSection = MutableStateFlow<Section?>(null)
    val selectedSection: StateFlow<Section?> = _selectedSection.asStateFlow()

    fun updateSelectedSection(section: Section?) {
        _selectedSection.value = section
        loadArticles(sectionKey = section?.key)
    }

    private var _selectionSheetState = mutableStateOf(false)
    var canShowSelection = _selectionSheetState
    fun closeSelection() {
        _selectionSheetState.value = false
    }
    fun openSelection() {
        _selectionSheetState.value = true
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

}