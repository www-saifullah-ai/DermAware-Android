package ai.saifullah.dermaware.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Stores the results of a photo-based skin analysis.
 * Each record represents one analysis session where the user
 * took or selected a photo and the TFLite model ran inference.
 */
@Entity(
    tableName = "analysis_results",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            // When a profile is deleted, delete all its analysis results too
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId")]
)
data class AnalysisResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Which profile this analysis belongs to
    val profileId: Long,

    // Body area analyzed (e.g., "face", "arm", "leg", "scalp", "hand", "foot")
    val bodyArea: String,

    // File path to the analyzed photo stored in app's cache directory
    val photoPath: String,

    // Top 3 condition results stored as a JSON string
    // Format: [{"conditionId":"ringworm","name":"Ringworm","confidence":0.85}, ...]
    val topResultsJson: String,

    // The highest-confidence condition ID from the results
    val topConditionId: String,

    // The highest confidence score (0.0 to 1.0)
    val topConfidence: Float,

    // Timestamp when this analysis was performed
    val analyzedAt: Long = System.currentTimeMillis(),

    // Any notes the user added to this result
    val userNotes: String = ""
)
