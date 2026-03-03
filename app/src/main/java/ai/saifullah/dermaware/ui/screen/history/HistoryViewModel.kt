package ai.saifullah.dermaware.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.saifullah.dermaware.data.database.entity.AnalysisResult
import ai.saifullah.dermaware.data.database.entity.SymptomResult
import ai.saifullah.dermaware.data.model.HistoryItem
import ai.saifullah.dermaware.data.repository.AnalysisRepository
import ai.saifullah.dermaware.data.repository.ProfileRepository
import ai.saifullah.dermaware.domain.SkinConditionDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the History screen.
 * Loads and manages the combined history of photo analyses and symptom checks.
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val analysisRepository: AnalysisRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // Combined history list (photo + symptom) for the active profile
    val historyItems: StateFlow<List<HistoryItem>> = profileRepository.activeProfile
        .flatMapLatest { profile ->
            if (profile == null) {
                flowOf(emptyList())
            } else {
                // Get combined history and resolve condition names from the database
                analysisRepository.getCombinedHistory(profile.id)
                    .map { items ->
                        items.map { item ->
                            // Replace condition ID with human-readable name
                            val conditionName = SkinConditionDatabase.getConditionById(item.topConditionId)?.name
                                ?: item.topConditionName
                            item.copy(topConditionName = conditionName)
                        }
                    }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // For viewing a detailed result from history
    private val _selectedPhotoResult = MutableStateFlow<AnalysisResult?>(null)
    val selectedPhotoResult: StateFlow<AnalysisResult?> = _selectedPhotoResult.asStateFlow()

    private val _selectedSymptomResult = MutableStateFlow<SymptomResult?>(null)
    val selectedSymptomResult: StateFlow<SymptomResult?> = _selectedSymptomResult.asStateFlow()

    fun loadPhotoResult(id: Long) {
        viewModelScope.launch {
            _selectedPhotoResult.value = analysisRepository.getPhotoResultById(id)
        }
    }

    fun loadSymptomResult(id: Long) {
        viewModelScope.launch {
            _selectedSymptomResult.value = analysisRepository.getSymptomResultById(id)
        }
    }

    /**
     * Delete a single history item (photo or symptom).
     */
    fun deleteHistoryItem(item: HistoryItem) {
        viewModelScope.launch {
            if (item.type == "photo") {
                val result = analysisRepository.getPhotoResultById(item.id) ?: return@launch
                analysisRepository.deletePhotoResult(result)
            } else {
                val result = analysisRepository.getSymptomResultById(item.id) ?: return@launch
                analysisRepository.deleteSymptomResult(result)
            }
        }
    }

    /**
     * Clear all history for the active profile.
     */
    fun clearAllHistory() {
        viewModelScope.launch {
            val profile = profileRepository.activeProfile.first() ?: return@launch
            analysisRepository.clearAllHistoryForProfile(profile.id)
        }
    }
}
