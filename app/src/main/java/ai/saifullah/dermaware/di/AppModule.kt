package ai.saifullah.dermaware.di

import android.content.Context
import androidx.room.Room
import ai.saifullah.dermaware.data.database.DermAwareDatabase
import ai.saifullah.dermaware.data.database.dao.AnalysisResultDao
import ai.saifullah.dermaware.data.database.dao.SymptomResultDao
import ai.saifullah.dermaware.data.database.dao.UserProfileDao
import ai.saifullah.dermaware.data.ml.MLManager
import ai.saifullah.dermaware.data.network.GeminiService
import ai.saifullah.dermaware.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt dependency injection module.
 * Tells Hilt how to create instances of classes that need to be injected.
 * SingletonComponent means these are created once and shared across the whole app.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the Room database instance.
     * Created once and reused throughout the app lifetime.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DermAwareDatabase {
        return DermAwareDatabase.getDatabase(context)
    }

    // Provide individual DAOs from the database
    @Provides
    @Singleton
    fun provideUserProfileDao(database: DermAwareDatabase): UserProfileDao {
        return database.userProfileDao()
    }

    @Provides
    @Singleton
    fun provideAnalysisResultDao(database: DermAwareDatabase): AnalysisResultDao {
        return database.analysisResultDao()
    }

    @Provides
    @Singleton
    fun provideSymptomResultDao(database: DermAwareDatabase): SymptomResultDao {
        return database.symptomResultDao()
    }

    /**
     * Provides the MLManager for TFLite inference.
     * Needs the application context to load model from assets.
     */
    @Provides
    @Singleton
    fun provideMLManager(@ApplicationContext context: Context): MLManager {
        return MLManager(context)
    }

    /**
     * Provides the GeminiService for online AI analysis via OpenRouter.
     * Reads the API key from BuildConfig (set in local.properties).
     * If the key is empty, the service reports isAvailable() = false
     * and the app silently uses TFLite instead.
     */
    @Provides
    @Singleton
    fun provideGeminiService(): GeminiService {
        return GeminiService(BuildConfig.OPENROUTER_API_KEY)
    }
}
