package ai.saifullah.dermaware.ui.screen.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.saifullah.dermaware.data.model.ConditionResult
import ai.saifullah.dermaware.data.model.PhotoAnalysisSession
import ai.saifullah.dermaware.data.repository.AnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

/**
 * ViewModel for the Results screen.
 * Loads a saved analysis result from the database by ID and parses the
 * stored JSON back into ConditionResult objects for display.
 */
@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val analysisRepository: AnalysisRepository
) : ViewModel() {

    private val _session = MutableStateFlow<PhotoAnalysisSession?>(null)
    val session: StateFlow<PhotoAnalysisSession?> = _session.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Load the analysis result from the database by its primary key ID.
     * Parses the stored topResultsJson back into a PhotoAnalysisSession.
     */
    fun loadResult(resultId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = analysisRepository.getPhotoResultById(resultId)
            if (result != null) {
                val conditions = parseResultsJson(result.topResultsJson)
                _session.value = PhotoAnalysisSession(
                    photoPath = result.photoPath,
                    bodyArea = result.bodyArea,
                    results = conditions,
                    isSuccessful = conditions.isNotEmpty()
                )
            }
            _isLoading.value = false
        }
    }

    /**
     * Parse the JSON string stored in the database back into ConditionResult objects.
     * Format: [{"conditionId":"...","name":"...","confidence":0.85,"category":"..."},...]
     */
    private fun parseResultsJson(json: String): List<ConditionResult> {
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length()).map { i ->
                val obj = jsonArray.getJSONObject(i)
                ConditionResult(
                    conditionId = obj.getString("conditionId"),
                    displayName = obj.getString("name"),
                    confidence = obj.getDouble("confidence").toFloat(),
                    category = obj.getString("category")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
