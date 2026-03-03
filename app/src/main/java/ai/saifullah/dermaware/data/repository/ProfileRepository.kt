package ai.saifullah.dermaware.data.repository

import ai.saifullah.dermaware.data.database.dao.UserProfileDao
import ai.saifullah.dermaware.data.database.entity.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for user profile operations.
 * The UI talks to this repository, not directly to the DAO.
 * This keeps the ViewModels clean and easy to test.
 */
@Singleton
class ProfileRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {
    // Live stream of all profiles — UI updates automatically when data changes
    val allProfiles: Flow<List<UserProfile>> = userProfileDao.getAllProfiles()

    // Live stream of the currently active profile
    val activeProfile: Flow<UserProfile?> = userProfileDao.getActiveProfile()

    suspend fun getProfileById(id: Long): UserProfile? = userProfileDao.getProfileById(id)

    suspend fun getProfileCount(): Int = userProfileDao.getProfileCount()

    // Create a new profile and return its ID
    suspend fun createProfile(name: String, ageGroup: String, skinType: String): Long {
        val profile = UserProfile(
            name = name,
            ageGroup = ageGroup,
            skinType = skinType,
            isActive = false
        )
        val newId = userProfileDao.insertProfile(profile)
        // If this is the first profile, make it active automatically
        if (userProfileDao.getProfileCount() == 1) {
            userProfileDao.setActiveProfile(newId)
        }
        return newId
    }

    suspend fun updateProfile(profile: UserProfile) = userProfileDao.updateProfile(profile)

    suspend fun deleteProfile(profile: UserProfile) = userProfileDao.deleteProfile(profile)

    suspend fun setActiveProfile(profileId: Long) = userProfileDao.setActiveProfile(profileId)
}
