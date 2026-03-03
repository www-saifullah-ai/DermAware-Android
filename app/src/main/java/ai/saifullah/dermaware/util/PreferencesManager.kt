package ai.saifullah.dermaware.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// Extension property to create a DataStore instance on the Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dermaware_preferences")

/**
 * Manages user preferences using DataStore (the modern, coroutine-friendly
 * replacement for SharedPreferences).
 *
 * Stores: onboarding completed flag, selected language, dark mode setting,
 * disclaimer acknowledged flag, monthly reminder enabled flag.
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Keys for each preference stored in DataStore
    companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val DISCLAIMER_ACKNOWLEDGED = booleanPreferencesKey("disclaimer_acknowledged")
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
        val DARK_MODE = stringPreferencesKey("dark_mode") // "system", "light", "dark"
        val MONTHLY_REMINDER_ENABLED = booleanPreferencesKey("monthly_reminder_enabled")
        val ACTIVE_PROFILE_ID = longPreferencesKey("active_profile_id")
    }

    // Live stream of all preferences — UI updates when any preference changes
    private val preferences: Flow<Preferences> = context.dataStore.data
        .catch { exception ->
            // If there's an error reading preferences, emit empty preferences
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    val onboardingCompleted: Flow<Boolean> = preferences.map { prefs ->
        prefs[ONBOARDING_COMPLETED] ?: false
    }

    val disclaimerAcknowledged: Flow<Boolean> = preferences.map { prefs ->
        prefs[DISCLAIMER_ACKNOWLEDGED] ?: false
    }

    val selectedLanguage: Flow<String> = preferences.map { prefs ->
        prefs[SELECTED_LANGUAGE] ?: "en"
    }

    val darkMode: Flow<String> = preferences.map { prefs ->
        prefs[DARK_MODE] ?: "system"
    }

    val monthlyReminderEnabled: Flow<Boolean> = preferences.map { prefs ->
        prefs[MONTHLY_REMINDER_ENABLED] ?: false
    }

    val activeProfileId: Flow<Long> = preferences.map { prefs ->
        prefs[ACTIVE_PROFILE_ID] ?: -1L
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setDisclaimerAcknowledged(acknowledged: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DISCLAIMER_ACKNOWLEDGED] = acknowledged
        }
    }

    suspend fun setSelectedLanguage(languageCode: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_LANGUAGE] = languageCode
        }
    }

    suspend fun setDarkMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE] = mode
        }
    }

    suspend fun setMonthlyReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MONTHLY_REMINDER_ENABLED] = enabled
        }
    }

    suspend fun setActiveProfileId(id: Long) {
        context.dataStore.edit { prefs ->
            prefs[ACTIVE_PROFILE_ID] = id
        }
    }
}
