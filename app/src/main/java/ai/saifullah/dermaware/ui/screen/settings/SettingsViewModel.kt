package ai.saifullah.dermaware.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.saifullah.dermaware.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 * Manages language, dark mode, and notification preferences.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val selectedLanguage: StateFlow<String> = preferencesManager.selectedLanguage
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "en")

    val darkMode: StateFlow<String> = preferencesManager.darkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")

    val monthlyReminderEnabled: StateFlow<Boolean> = preferencesManager.monthlyReminderEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // List of supported languages (code, English name, native name)
    val supportedLanguages = listOf(
        Triple("en", "English", "English"),
        Triple("bn", "Bengali", "বাংলা"),
        Triple("hi", "Hindi", "हिन्दी"),
        Triple("ur", "Urdu", "اردو"),
        Triple("ar", "Arabic", "العربية"),
        Triple("fr", "French", "Français"),
        Triple("es", "Spanish", "Español"),
        Triple("pt", "Portuguese", "Português"),
        Triple("sw", "Swahili", "Kiswahili"),
        Triple("ha", "Hausa", "Hausa"),
        Triple("am", "Amharic", "አማርኛ"),
        Triple("id", "Indonesian", "Indonesia"),
        Triple("ms", "Malay", "Bahasa Melayu"),
        Triple("tr", "Turkish", "Türkçe"),
        Triple("pa", "Punjabi", "ਪੰਜਾਬੀ"),
        Triple("ta", "Tamil", "தமிழ்"),
        Triple("te", "Telugu", "తెలుగు"),
        Triple("mr", "Marathi", "मराठी"),
        Triple("gu", "Gujarati", "ગુજરાતી"),
        Triple("kn", "Kannada", "ಕನ್ನಡ"),
        Triple("ml", "Malayalam", "മലയാളം"),
        Triple("si", "Sinhala", "සිංහල"),
        Triple("ne", "Nepali", "नेपाली"),
        Triple("my", "Burmese", "မြန်မာဘာသာ"),
        Triple("th", "Thai", "ภาษาไทย"),
        Triple("vi", "Vietnamese", "Tiếng Việt"),
        Triple("fil", "Filipino", "Filipino"),
        Triple("fa", "Persian", "فارسی"),
        Triple("ps", "Pashto", "پښتو"),
        Triple("so", "Somali", "Somali"),
        Triple("yo", "Yoruba", "Yorùbá"),
        Triple("ig", "Igbo", "Igbo")
    )

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            preferencesManager.setSelectedLanguage(languageCode)
        }
    }

    fun setDarkMode(mode: String) {
        viewModelScope.launch {
            preferencesManager.setDarkMode(mode)
        }
    }

    fun setMonthlyReminder(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setMonthlyReminderEnabled(enabled)
            // WorkManager scheduling would be triggered here
        }
    }
}
