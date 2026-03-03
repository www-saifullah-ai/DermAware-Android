package ai.saifullah.dermaware.data.database.dao

import androidx.room.*
import ai.saifullah.dermaware.data.database.entity.SymptomResult
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for SymptomResult (text questionnaire check records).
 */
@Dao
interface SymptomResultDao {

    // Get all symptom results for a profile, newest first
    @Query("SELECT * FROM symptom_results WHERE profileId = :profileId ORDER BY checkedAt DESC")
    fun getResultsForProfile(profileId: Long): Flow<List<SymptomResult>>

    // Get a single symptom result by ID
    @Query("SELECT * FROM symptom_results WHERE id = :id")
    suspend fun getResultById(id: Long): SymptomResult?

    // Insert a new symptom check result
    @Insert
    suspend fun insertResult(result: SymptomResult): Long

    // Delete a single symptom result
    @Delete
    suspend fun deleteResult(result: SymptomResult)

    // Delete all symptom results for a profile
    @Query("DELETE FROM symptom_results WHERE profileId = :profileId")
    suspend fun deleteAllResultsForProfile(profileId: Long)
}
