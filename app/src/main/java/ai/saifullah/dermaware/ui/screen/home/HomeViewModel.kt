package ai.saifullah.dermaware.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.saifullah.dermaware.data.database.entity.UserProfile
import ai.saifullah.dermaware.data.repository.AnalysisRepository
import ai.saifullah.dermaware.data.repository.ProfileRepository
import ai.saifullah.dermaware.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen.
 * Provides the active profile and basic stats (analysis count) to the UI.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val analysisRepository: AnalysisRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // Active profile shown in the home screen header
    val activeProfile: StateFlow<UserProfile?> = profileRepository.activeProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Whether the app has profiles set up at all
    val hasProfiles: StateFlow<Boolean> = profileRepository.allProfiles
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Whether onboarding has been completed
    val onboardingCompleted: StateFlow<Boolean> = preferencesManager.onboardingCompleted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Whether the full-screen disclaimer has been acknowledged
    val disclaimerAcknowledged: StateFlow<Boolean> = preferencesManager.disclaimerAcknowledged
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        // Auto-create a "Default" profile on first launch so the app is usable immediately
        viewModelScope.launch {
            if (profileRepository.getProfileCount() == 0) {
                profileRepository.createProfile(
                    name = "Default",
                    ageGroup = "adult",
                    skinType = "unknown"
                )
            }
        }
    }

    fun acknowledgeDisclaimer() {
        viewModelScope.launch {
            preferencesManager.setDisclaimerAcknowledged(true)
        }
    }
}
