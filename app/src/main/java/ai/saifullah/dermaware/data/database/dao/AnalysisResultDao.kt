package ai.saifullah.dermaware.data.database.dao

import androidx.room.*
import ai.saifullah.dermaware.data.database.entity.AnalysisResult
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for AnalysisResult (photo-based skin analysis records).
 */
@Dao
interface AnalysisResultDao {

    // Get all analysis results for a profile, newest first
    @Query("SELECT * FROM analysis_results WHERE profileId = :profileId ORDER BY analyzedAt DESC")
    fun getResultsForProfile(profileId: Long): Flow<List<AnalysisResult>>

    // Get a single result by ID (for viewing result detail)
    @Query("SELECT * FROM analysis_results WHERE id = :id")
    suspend fun getResultById(id: Long): AnalysisResult?

    // Get total count of results for a profile
    @Query("SELECT COUNT(*) FROM analysis_results WHERE profileId = :profileId")
    suspend fun getResultCountForProfile(profileId: Long): Int

    // Insert a new analysis result and return its ID
    @Insert
    suspend fun insertResult(result: AnalysisResult): Long

    // Update an existing result (e.g., user adds notes)
    @Update
    suspend fun updateResult(result: AnalysisResult)

    // Delete a single result
    @Delete
    suspend fun deleteResult(result: AnalysisResult)

    // Delete all results for a specific profile
    @Query("DELETE FROM analysis_results WHERE profileId = :profileId")
    suspend fun deleteAllResultsForProfile(profileId: Long)
}
