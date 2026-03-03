package ai.saifullah.dermaware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import ai.saifullah.dermaware.ui.navigation.DermAwareNavHost
import ai.saifullah.dermaware.ui.theme.DermAwareTheme
import ai.saifullah.dermaware.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The single Activity for DermAware.
 * Uses Jetpack Navigation with a single NavHost — no multiple activities needed.
 *
 * @AndroidEntryPoint enables Hilt dependency injection in this Activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Show the splash screen with the app icon before the UI loads
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Collect dark mode preference to apply the correct theme
            val darkModePref by preferencesManager.darkMode.collectAsState(initial = "system")
            val isSystemDark = isSystemInDarkTheme()
            val isDarkTheme = when (darkModePref) {
                "dark" -> true
                "light" -> false
                else -> isSystemDark  // "system" — follow the device setting
            }

            // Check if we need to show onboarding
            var showOnboarding by remember { mutableStateOf(false) }
            var isReady by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                // Read onboarding completed flag once on startup
                val onboardingDone = preferencesManager.onboardingCompleted.first()
                showOnboarding = !onboardingDone
                if (!onboardingDone) {
                    // Mark onboarding as completed after we show it
                    preferencesManager.setOnboardingCompleted(true)
                }
                isReady = true
            }

            if (isReady) {
                DermAwareTheme(darkTheme = isDarkTheme) {
                    DermAwareNavHost(
                        preferencesManager = preferencesManager,
                        startFromOnboarding = showOnboarding
                    )
                }
            }
        }
    }
}
