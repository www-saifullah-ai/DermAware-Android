package ai.saifullah.dermaware.data.repository

import ai.saifullah.dermaware.data.database.dao.AnalysisResultDao
import ai.saifullah.dermaware.data.database.dao.SymptomResultDao
import ai.saifullah.dermaware.data.database.entity.AnalysisResult
import ai.saifullah.dermaware.data.database.entity.SymptomResult
import ai.saifullah.dermaware.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for analysis results (both photo and symptom).
 * Also provides the combined history list shown on the History screen.
 */
@Singleton
class AnalysisRepository @Inject constructor(
    private val analysisResultDao: AnalysisResultDao,
    private val symptomResultDao: SymptomResultDao
) {
    // Get photo analysis results for a profile as a live stream
    fun getPhotoResultsForProfile(profileId: Long): Flow<List<AnalysisResult>> =
        analysisResultDao.getResultsForProfile(profileId)

    // Get symptom check results for a profile as a live stream
    fun getSymptomResultsForProfile(profileId: Long): Flow<List<SymptomResult>> =
        symptomResultDao.getResultsForProfile(profileId)

    /**
     * Returns a combined, sorted history list of both photo and symptom results.
     * Both types appear together in the History screen, newest first.
     */
    fun getCombinedHistory(profileId: Long): Flow<List<HistoryItem>> {
        val photoFlow = analysisResultDao.getResultsForProfile(profileId)
            .map { list ->
                list.map { result ->
                    HistoryItem(
                        id = result.id,
                        type = "photo",
                        bodyArea = result.bodyArea,
                        topConditionName = result.topConditionId, // resolved in ViewModel
                        topConditionId = result.topConditionId,
                        timestamp = result.analyzedAt,
                        photoPath = result.photoPath
                    )
                }
            }

        val symptomFlow = symptomResultDao.getResultsForProfile(profileId)
            .map { list ->
                list.map { result ->
                    HistoryItem(
                        id = result.id,
                        type = "symptom",
                        bodyArea = result.bodyArea,
                        topConditionName = result.topConditionId,
                        topConditionId = result.topConditionId,
                        timestamp = result.checkedAt
                    )
                }
            }

        // Combine both lists and sort by timestamp, newest first
        return combine(photoFlow, symptomFlow) { photos, symptoms ->
            (photos + symptoms).sortedByDescending { it.timestamp }
        }
    }

    suspend fun getPhotoResultById(id: Long): AnalysisResult? =
        analysisResultDao.getResultById(id)

    suspend fun getSymptomResultById(id: Long): SymptomResult? =
        symptomResultDao.getResultById(id)

    suspend fun savePhotoAnalysis(result: AnalysisResult): Long =
        analysisResultDao.insertResult(result)

    suspend fun saveSymptomResult(result: SymptomResult): Long =
        symptomResultDao.insertResult(result)

    suspend fun deletePhotoResult(result: AnalysisResult) =
        analysisResultDao.deleteResult(result)

    suspend fun deleteSymptomResult(result: SymptomResult) =
        symptomResultDao.deleteResult(result)

    suspend fun clearAllHistoryForProfile(profileId: Long) {
        analysisResultDao.deleteAllResultsForProfile(profileId)
        symptomResultDao.deleteAllResultsForProfile(profileId)
    }
}
