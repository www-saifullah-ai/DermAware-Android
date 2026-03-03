package ai.saifullah.dermaware.ui.screen.library

import androidx.lifecycle.ViewModel
import ai.saifullah.dermaware.data.model.SkinCondition
import ai.saifullah.dermaware.domain.SkinConditionDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel for the Condition Library screen.
 * Manages search, filtering, and the list of conditions.
 */
@HiltViewModel
class LibraryViewModel @Inject constructor() : ViewModel() {

    // Current search query typed by the user
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Currently selected category filter ("All", "Infection", "Inflammatory", etc.)
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // All available category options
    val categories: List<String> = SkinConditionDatabase.getAllCategories()

    // Filtered/searched conditions to display
    private val _displayedConditions = MutableStateFlow(SkinConditionDatabase.getAllConditions())
    val displayedConditions: StateFlow<List<SkinCondition>> = _displayedConditions.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateDisplayedConditions()
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        updateDisplayedConditions()
    }

    /**
     * Filter conditions based on the current search query and selected category.
     * Both filters work together — a condition must match both to appear.
     */
    private fun updateDisplayedConditions() {
        val query = _searchQuery.value
        val category = _selectedCategory.value

        var filtered = if (query.isBlank()) {
            SkinConditionDatabase.getAllConditions()
        } else {
            SkinConditionDatabase.searchConditions(query)
        }

        if (category != "All") {
            filtered = filtered.filter { it.category == category }
        }

        _displayedConditions.value = filtered
    }
}
