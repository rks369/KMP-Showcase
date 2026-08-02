package org.rks369.news.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModal : ViewModel() {
    val heading : String = "News"

    val sectionsAvailable: List<Section> = getSectionsAvailableForNewYorkNews()
    private val _selectedSection = MutableStateFlow<Section?>(null)
    val selectedSection: StateFlow<Section?> = _selectedSection.asStateFlow()

    fun updateSelectedSection(section: Section?) {
        _selectedSection.value = section   // or however you're holding selected state
    }

    private var _selectionSheetState = mutableStateOf(false)
    var canShowSelection = _selectionSheetState
    fun closeSelection() {
        _selectionSheetState.value = false
    }
    fun openSelection() {
        _selectionSheetState.value = true
    }

    private  fun getSectionsAvailableForNewYorkNews(): List<Section> = listOf(
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

}

data class Section(
    val key: String?,
    val title: String?,
    val description: String? = null,
    var isActive: Boolean = false
)

