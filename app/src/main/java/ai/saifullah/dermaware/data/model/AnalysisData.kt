package ai.saifullah.dermaware.data.model

/**
 * Represents a single condition result from the TFLite model.
 * The model returns the top 3 conditions with confidence scores.
 */
data class ConditionResult(
    // The unique ID that maps to the skin condition database (e.g., "ringworm")
    val conditionId: String,
    // Human-readable name of the condition (e.g., "Ringworm (Tinea Corporis)")
    val displayName: String,
    // Confidence score from 0.0 to 1.0 (e.g., 0.85 means 85% confident)
    val confidence: Float,
    // Category of the condition (e.g., "Infection", "Inflammatory")
    val category: String
)

/**
 * Contains everything about a completed photo analysis session.
 * Passed from the AnalyzeViewModel to the ResultsScreen.
 */
data class PhotoAnalysisSession(
    // Path to the analyzed photo file
    val photoPath: String,
    // Body area that was photographed
    val bodyArea: String,
    // Top 3 matching conditions, sorted by confidence (highest first)
    val results: List<ConditionResult>,
    // Whether the model ran successfully or hit an error
    val isSuccessful: Boolean,
    // Error message if isSuccessful is false
    val errorMessage: String = "",
    // Gemini's 1-2 sentence educational observation (empty if TFLite was used)
    val aiSummary: String = "",
    // true = analyzed by Gemini online, false = analyzed by TFLite on-device
    val isOnlineAnalysis: Boolean = false,
    // Message shown when Gemini failed and we fell back to TFLite (empty = no fallback happened)
    val fallbackMessage: String = ""
)

/**
 * Represents a history item shown in the History screen.
 * Combines data from both analysis and symptom result tables.
 */
data class HistoryItem(
    val id: Long,
    // "photo" for photo analysis, "symptom" for symptom checker
    val type: String,
    val bodyArea: String,
    val topConditionName: String,
    val topConditionId: String,
    val timestamp: Long,
    // Only for photo analysis items
    val photoPath: String? = null
)

/**
 * Represents the urgency level for seeking medical help.
 * Every skin condition in the database has one of these levels.
 */
enum class UrgencyLevel {
    LOW,       // Monitor at home — no rush
    MEDIUM,    // See a doctor within a week
    HIGH,      // See a doctor within 1-2 days
    EMERGENCY  // Seek immediate medical care
}
