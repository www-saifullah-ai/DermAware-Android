package ai.saifullah.dermaware.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.saifullah.dermaware.data.database.entity.UserProfile
import ai.saifullah.dermaware.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Profile Management screen.
 * Handles creating, editing, switching, and deleting profiles.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // Live list of all profiles
    val profiles: StateFlow<List<UserProfile>> = profileRepository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently active profile
    val activeProfile: StateFlow<UserProfile?> = profileRepository.activeProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // UI feedback messages
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    /**
     * Create a new profile.
     * If it's the first profile, it automatically becomes active.
     */
    fun createProfile(name: String, ageGroup: String, skinType: String) {
        if (name.isBlank()) {
            _message.value = "Name cannot be empty"
            return
        }
        viewModelScope.launch {
            profileRepository.createProfile(name.trim(), ageGroup, skinType)
            _message.value = "Profile created"
        }
    }

    /**
     * Switch the currently active profile.
     */
    fun switchToProfile(profileId: Long) {
        viewModelScope.launch {
            profileRepository.setActiveProfile(profileId)
        }
    }

    /**
     * Delete a profile. Cannot delete if it's the only profile.
     */
    fun deleteProfile(profile: UserProfile) {
        viewModelScope.launch {
            val count = profileRepository.getProfileCount()
            if (count <= 1) {
                _message.value = "Cannot delete the only profile"
                return@launch
            }
            profileRepository.deleteProfile(profile)
            _message.value = "Profile deleted"
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
