package ai.saifullah.dermaware.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Stores the results of a symptom-based (text questionnaire) check.
 * Users answer questions about their symptoms without needing a photo.
 */
@Entity(
    tableName = "symptom_results",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId")]
)
data class SymptomResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Which profile this symptom check belongs to
    val profileId: Long,

    // Body area affected (from questionnaire answer)
    val bodyArea: String,

    // Duration of symptoms (e.g., "less_than_week", "1_2_weeks", "more_than_month")
    val duration: String,

    // All questionnaire answers stored as JSON
    // Format: {"bodyArea":"arm","duration":"1week","color":"red","texture":"scaly",...}
    val answersJson: String,

    // Matching conditions stored as JSON (same format as AnalysisResult.topResultsJson)
    val matchingConditionsJson: String,

    // The top matching condition ID
    val topConditionId: String,

    // Timestamp of when the symptom check was completed
    val checkedAt: Long = System.currentTimeMillis()
)
