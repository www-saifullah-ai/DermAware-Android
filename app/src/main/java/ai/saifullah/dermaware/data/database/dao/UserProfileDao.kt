package ai.saifullah.dermaware.data.database.dao

import androidx.room.*
import ai.saifullah.dermaware.data.database.entity.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for UserProfile.
 * Provides methods to create, read, update, and delete profiles.
 * Flow<> types automatically update the UI when database data changes.
 */
@Dao
interface UserProfileDao {

    // Get all profiles as a live stream — UI updates whenever profiles change
    @Query("SELECT * FROM user_profiles ORDER BY createdAt ASC")
    fun getAllProfiles(): Flow<List<UserProfile>>

    // Get the currently active profile
    @Query("SELECT * FROM user_profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveProfile(): Flow<UserProfile?>

    // Get a profile by its ID (used when viewing history)
    @Query("SELECT * FROM user_profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): UserProfile?

    // Count how many profiles exist
    @Query("SELECT COUNT(*) FROM user_profiles")
    suspend fun getProfileCount(): Int

    // Insert a new profile and return its new ID
    @Insert
    suspend fun insertProfile(profile: UserProfile): Long

    // Update an existing profile (e.g., user edits their name)
    @Update
    suspend fun updateProfile(profile: UserProfile)

    // Delete a specific profile (also deletes all its history via CASCADE)
    @Delete
    suspend fun deleteProfile(profile: UserProfile)

    // Set one profile as active and deactivate all others
    @Transaction
    suspend fun setActiveProfile(profileId: Long) {
        deactivateAllProfiles()
        activateProfile(profileId)
    }

    @Query("UPDATE user_profiles SET isActive = 0")
    suspend fun deactivateAllProfiles()

    @Query("UPDATE user_profiles SET isActive = 1 WHERE id = :profileId")
    suspend fun activateProfile(profileId: Long)
}
