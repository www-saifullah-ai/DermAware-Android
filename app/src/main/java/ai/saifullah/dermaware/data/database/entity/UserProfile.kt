package ai.saifullah.dermaware.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user profile stored in the local Room database.
 * Families can create multiple profiles (e.g., one for each family member).
 * No account or internet required — all data stays on the device.
 */
@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Profile name (e.g., "Mom", "Child", or a real name)
    val name: String,

    // Age group — used to tailor condition information
    // Values: "child", "teen", "adult", "senior"
    val ageGroup: String,

    // Skin type — Fitzpatrick scale for better ML results
    // Values: "type1", "type2", "type3", "type4", "type5", "type6"
    val skinType: String = "unknown",

    // Timestamp when this profile was created
    val createdAt: Long = System.currentTimeMillis(),

    // Whether this is the currently active profile
    val isActive: Boolean = false
)
