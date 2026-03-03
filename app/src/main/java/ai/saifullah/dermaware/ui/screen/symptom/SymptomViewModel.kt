package ai.saifullah.dermaware.ui.screen.symptom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.saifullah.dermaware.data.database.entity.SymptomResult
import ai.saifullah.dermaware.data.model.ConditionResult
import ai.saifullah.dermaware.data.repository.AnalysisRepository
import ai.saifullah.dermaware.data.repository.ProfileRepository
import ai.saifullah.dermaware.domain.SkinConditionDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Symptom Checker questionnaire.
 * Manages the step-by-step question flow and matches conditions based on answers.
 */
@HiltViewModel
class SymptomViewModel @Inject constructor(
    private val analysisRepository: AnalysisRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // Current step in the questionnaire (0-based index)
    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    // All collected answers
    val answers = mutableMapOf<String, String>()

    // Matching results after questionnaire is complete
    private val _results = MutableStateFlow<List<ConditionResult>>(emptyList())
    val results: StateFlow<List<ConditionResult>> = _results.asStateFlow()

    // Whether questionnaire is complete and results are showing
    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete.asStateFlow()

    // Total number of steps in the questionnaire
    val totalSteps = 6

    // Question definitions for each step
    data class Question(
        val key: String,
        val question: String,
        val options: List<String>
    )

    val questions = listOf(
        Question(
            key = "bodyArea",
            question = "Which part of the body is affected?",
            options = listOf("Face", "Scalp", "Neck", "Arms/Hands", "Trunk/Chest/Back", "Legs/Feet", "Groin/Genitals", "Multiple areas")
        ),
        Question(
            key = "duration",
            question = "How long have you had this?",
            options = listOf("Less than 3 days", "3 days to 1 week", "1 to 4 weeks", "1 to 3 months", "More than 3 months")
        ),
        Question(
            key = "appearance",
            question = "What does it look like?",
            options = listOf("Red rash/patch", "Raised bumps", "Flat discoloration", "Scaly/flaking", "Blisters/fluid-filled", "Crusty/weeping", "Dark/brown patch", "White/light patch")
        ),
        Question(
            key = "symptoms",
            question = "What symptoms do you have? (pick the main one)",
            options = listOf("Intense itching", "Burning or stinging", "Pain or tenderness", "No discomfort", "Itching + burning", "Swelling and redness")
        ),
        Question(
            key = "spreading",
            question = "Is it spreading or changing?",
            options = listOf("Spreading to new areas", "Getting bigger at edges", "Stable — not changing", "Getting better on its own", "Comes and goes")
        ),
        Question(
            key = "ageGroup",
            question = "What is the age of the affected person?",
            options = listOf("Infant (0-2 years)", "Child (3-12 years)", "Teen (13-17 years)", "Young adult (18-35)", "Adult (36-60)", "Senior (60+)")
        )
    )

    fun answerQuestion(key: String, answer: String) {
        answers[key] = answer
    }

    fun nextStep() {
        if (_currentStep.value < totalSteps - 1) {
            _currentStep.value += 1
        } else {
            // All questions answered — compute results
            computeResults()
        }
    }

    fun previousStep() {
        if (_currentStep.value > 0) {
            _currentStep.value -= 1
        }
    }

    /**
     * Simple rule-based symptom matching.
     * Matches conditions based on the user's answers using scoring logic.
     * Each condition gets points when its characteristics match the answers.
     */
    private fun computeResults() {
        val allConditions = SkinConditionDatabase.getAllConditions()
            .filter { it.id != "normal_healthy_skin" }

        // Score each condition based on how well it matches the answers
        val scoredConditions = allConditions.map { condition ->
            var score = 0

            // Score based on appearance answer
            val appearance = answers["appearance"]?.lowercase() ?: ""
            val symptoms = answers["symptoms"]?.lowercase() ?: ""

            when (condition.category) {
                "Infection" -> {
                    if (appearance.contains("scaly") || appearance.contains("crusted")) score += 2
                    if (appearance.contains("bumps") || appearance.contains("blisters")) score += 2
                    if (symptoms.contains("itch")) score += 1
                }
                "Inflammatory" -> {
                    if (appearance.contains("red") || appearance.contains("raised")) score += 2
                    if (symptoms.contains("itch") || symptoms.contains("burn")) score += 2
                    if (condition.id == "atopic_dermatitis" && symptoms.contains("itch")) score += 3
                    if (condition.id == "psoriasis" && appearance.contains("scaly")) score += 3
                    if (condition.id == "contact_dermatitis" && symptoms.contains("burn")) score += 2
                    if (condition.id == "urticaria" && appearance.contains("raised")) score += 3
                }
                "Pigmentation" -> {
                    if (appearance.contains("discolor") || appearance.contains("dark") || appearance.contains("white")) score += 3
                    if (symptoms.contains("no discomfort")) score += 2
                    if (condition.id == "vitiligo" && appearance.contains("white")) score += 3
                    if (condition.id == "melasma" && appearance.contains("dark")) score += 3
                    if (condition.id == "post_inflammatory_hyperpigmentation" && appearance.contains("dark")) score += 2
                }
                "Dangerous" -> {
                    // Only suggest dangerous conditions if there are strong indicators
                    if (appearance.contains("dark") && answers["spreading"]?.contains("bigger") == true) score += 2
                    if (appearance.contains("flat discolor") && answers["duration"]?.contains("month") == true) score += 2
                }
                "Other" -> {
                    score += 1  // Baseline for other conditions
                }
            }

            // Adjust for duration
            val duration = answers["duration"]?.lowercase() ?: ""
            if (duration.contains("more than 3 months")) {
                if (condition.category in listOf("Inflammatory", "Pigmentation")) score += 1
            }

            // Adjust for spreading
            val spreading = answers["spreading"]?.lowercase() ?: ""
            if (spreading.contains("spreading") && condition.isContagious) score += 1

            Pair(condition, score)
        }

        // Take top 5 conditions with highest score
        val topConditions = scoredConditions
            .filter { (_, score) -> score > 0 }
            .sortedByDescending { (_, score) -> score }
            .take(5)
            .map { (condition, score) ->
                // Convert score to a pseudo-confidence (0.0 - 1.0 range)
                val maxScore = scoredConditions.maxOf { it.second }.coerceAtLeast(1)
                val confidence = (score.toFloat() / maxScore.toFloat()).coerceIn(0.1f, 0.9f)
                ConditionResult(
                    conditionId = condition.id,
                    displayName = condition.name,
                    confidence = confidence,
                    category = condition.category
                )
            }

        _results.value = topConditions
        _isComplete.value = true

        // Save to database
        saveSymptomResults(topConditions)
    }

    private fun saveSymptomResults(results: List<ConditionResult>) {
        viewModelScope.launch {
            val activeProfile = profileRepository.activeProfile.first() ?: return@launch
            if (results.isEmpty()) return@launch

            val answersJson = answers.entries.joinToString(",", "{", "}") { (k, v) ->
                "\"$k\":\"$v\""
            }
            val resultsJson = results.joinToString(",", "[", "]") { result ->
                """{"conditionId":"${result.conditionId}","name":"${result.displayName}","confidence":${result.confidence},"category":"${result.category}"}"""
            }

            val symptomResult = SymptomResult(
                profileId = activeProfile.id,
                bodyArea = answers["bodyArea"] ?: "unknown",
                duration = answers["duration"] ?: "unknown",
                answersJson = answersJson,
                matchingConditionsJson = resultsJson,
                topConditionId = results.first().conditionId
            )
            analysisRepository.saveSymptomResult(symptomResult)
        }
    }

    fun reset() {
        _currentStep.value = 0
        _isComplete.value = false
        _results.value = emptyList()
        answers.clear()
    }
}
