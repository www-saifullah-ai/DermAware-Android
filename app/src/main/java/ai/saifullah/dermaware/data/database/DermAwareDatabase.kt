package ai.saifullah.dermaware.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ai.saifullah.dermaware.data.database.dao.AnalysisResultDao
import ai.saifullah.dermaware.data.database.dao.SymptomResultDao
import ai.saifullah.dermaware.data.database.dao.UserProfileDao
import ai.saifullah.dermaware.data.database.entity.AnalysisResult
import ai.saifullah.dermaware.data.database.entity.SymptomResult
import ai.saifullah.dermaware.data.database.entity.UserProfile

/**
 * The main Room database for DermAware.
 * Contains all tables for user profiles, analysis results, and symptom results.
 *
 * All data is stored LOCALLY on the device — no internet required.
 * The database is a single file on the device's internal storage.
 */
@Database(
    entities = [
        UserProfile::class,
        AnalysisResult::class,
        SymptomResult::class
    ],
    version = 1,
    exportSchema = true  // Exports schema to /schemas/ folder for migration tracking
)
abstract class DermAwareDatabase : RoomDatabase() {

    // DAOs — Room generates the implementation automatically
    abstract fun userProfileDao(): UserProfileDao
    abstract fun analysisResultDao(): AnalysisResultDao
    abstract fun symptomResultDao(): SymptomResultDao

    companion object {
        // Volatile ensures the instance is always up-to-date across threads
        @Volatile
        private var INSTANCE: DermAwareDatabase? = null

        fun getDatabase(context: Context): DermAwareDatabase {
            // If the database already exists, return it — no need to create again
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DermAwareDatabase::class.java,
                    "dermaware_database"
                )
                    // If the user upgrades the app and the database schema changes,
                    // fall back to recreating the database rather than crashing
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
