package ai.saifullah.dermaware.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.*
import androidx.navigation.compose.*
import androidx.hilt.navigation.compose.hiltViewModel
import ai.saifullah.dermaware.ui.screen.analyze.AnalyzeScreen
import ai.saifullah.dermaware.ui.screen.analyze.AnalyzeViewModel
import ai.saifullah.dermaware.ui.screen.analyze.ResultsScreen
import ai.saifullah.dermaware.ui.screen.education.EducationScreen
import ai.saifullah.dermaware.ui.screen.history.HistoryScreen
import ai.saifullah.dermaware.ui.screen.home.HomeScreen
import ai.saifullah.dermaware.ui.screen.library.ConditionDetailScreen
import ai.saifullah.dermaware.ui.screen.library.LibraryScreen
import ai.saifullah.dermaware.ui.screen.onboarding.OnboardingScreen
import ai.saifullah.dermaware.ui.screen.profile.ProfileScreen
import ai.saifullah.dermaware.ui.screen.settings.SettingsScreen
import ai.saifullah.dermaware.ui.screen.aboutdeveloper.AboutDeveloperScreen
import ai.saifullah.dermaware.ui.screen.symptom.SymptomCheckerScreen
import ai.saifullah.dermaware.util.PreferencesManager

// Navigation route constants — all screen destinations as strings
object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val ANALYZE = "analyze"
    const val RESULTS = "results"
    const val SYMPTOM_CHECKER = "symptom_checker"
    const val LIBRARY = "library"
    const val CONDITION_DETAIL = "condition_detail/{conditionId}"
    const val HISTORY = "history"
    const val EDUCATION = "education"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val ABOUT_DEVELOPER = "about_developer"

    // Helper to build the condition detail route with an argument
    fun conditionDetail(conditionId: String) = "condition_detail/$conditionId"
}

// Bottom navigation tab definitions
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Routes.HOME, Icons.Default.Home, "Home"),
    BottomNavItem(Routes.ANALYZE, Icons.Default.CameraAlt, "Analyze"),
    BottomNavItem(Routes.LIBRARY, Icons.Default.MenuBook, "Library"),
    BottomNavItem(Routes.HISTORY, Icons.Default.History, "History")
)

/**
 * Main navigation host for the entire DermAware app.
 * Controls which screen is shown and handles back stack.
 *
 * The bottom navigation bar is shown on the 4 main tabs only.
 * All other screens (analyze, results, detail) are full-screen without the bottom bar.
 */
@Composable
fun DermAwareNavHost(
    preferencesManager: PreferencesManager,
    startFromOnboarding: Boolean
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Routes where the bottom navigation bar should be visible
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(item.icon, contentDescription = item.label)
                            },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        // Pop up to home to avoid a large back stack
                                        popUpTo(Routes.HOME) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (startFromOnboarding) Routes.ONBOARDING else Routes.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {

            // Onboarding — only shown on first launch
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onComplete = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }

            // Main bottom nav tabs
            composable(Routes.HOME) {
                HomeScreen(
                    onNavigateToAnalyze = { navController.navigate(Routes.ANALYZE) },
                    onNavigateToSymptomChecker = { navController.navigate(Routes.SYMPTOM_CHECKER) },
                    onNavigateToEducation = { navController.navigate(Routes.EDUCATION) },
                    onNavigateToProfiles = { navController.navigate(Routes.PROFILE) },
                    onNavigateToSettings = { navController.navigate(Routes.SETTINGS) }
                )
            }

            composable(Routes.ANALYZE) {
                AnalyzeScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToResults = {
                        navController.navigate(Routes.RESULTS) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Results screen — shares the AnalyzeViewModel with the Analyze screen
            // so it can read the analysis results directly from memory (no DB dependency).
            composable(Routes.RESULTS) {
                val analyzeEntry = remember(it) {
                    navController.getBackStackEntry(Routes.ANALYZE)
                }
                val sharedViewModel: AnalyzeViewModel = hiltViewModel(analyzeEntry)
                ResultsScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToConditionDetail = { conditionId ->
                        navController.navigate(Routes.conditionDetail(conditionId))
                    },
                    onAnalyzeAnother = {
                        navController.navigate(Routes.ANALYZE) {
                            popUpTo(Routes.HOME)
                        }
                    },
                    viewModel = sharedViewModel
                )
            }

            composable(Routes.SYMPTOM_CHECKER) {
                SymptomCheckerScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToConditionDetail = { conditionId ->
                        navController.navigate(Routes.conditionDetail(conditionId))
                    }
                )
            }

            composable(Routes.LIBRARY) {
                LibraryScreen(
                    onNavigateToConditionDetail = { conditionId ->
                        navController.navigate(Routes.conditionDetail(conditionId))
                    }
                )
            }

            // Condition detail — accessed from Library, Results, and Symptom Checker
            composable(
                route = Routes.CONDITION_DETAIL,
                arguments = listOf(navArgument("conditionId") { type = NavType.StringType })
            ) { backStackEntry ->
                val conditionId = backStackEntry.arguments?.getString("conditionId") ?: return@composable
                ConditionDetailScreen(
                    conditionId = conditionId,
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composable(Routes.HISTORY) {
                HistoryScreen(
                    onNavigateToConditionDetail = { conditionId ->
                        navController.navigate(Routes.conditionDetail(conditionId))
                    }
                )
            }

            composable(Routes.EDUCATION) {
                EducationScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToAboutDeveloper = { navController.navigate(Routes.ABOUT_DEVELOPER) }
                )
            }

            composable(Routes.ABOUT_DEVELOPER) {
                AboutDeveloperScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}
