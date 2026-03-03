package ai.saifullah.dermaware.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ai.saifullah.dermaware.BuildConfig

/**
 * Settings screen — language, dark mode, notification preferences, privacy info.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAboutDeveloper: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val darkMode by viewModel.darkMode.collectAsState()
    val monthlyReminderEnabled by viewModel.monthlyReminderEnabled.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showDarkModeDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = selectedLanguage,
            languages = viewModel.supportedLanguages,
            onSelect = { viewModel.setLanguage(it); showLanguageDialog = false },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showDarkModeDialog) {
        AlertDialog(
            onDismissRequest = { showDarkModeDialog = false },
            title = { Text("Appearance") },
            text = {
                Column {
                    listOf("system" to "System Default", "light" to "Light Mode", "dark" to "Dark Mode")
                        .forEach { (mode, label) ->
                            ListItem(
                                headlineContent = { Text(label) },
                                leadingContent = {
                                    RadioButton(
                                        selected = darkMode == mode,
                                        onClick = { viewModel.setDarkMode(mode) }
                                    )
                                },
                                modifier = Modifier.clickableCompat { viewModel.setDarkMode(mode) }
                            )
                        }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDarkModeDialog = false }) { Text("Done") }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Policy") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("DermAware Privacy Policy", fontWeight = FontWeight.Bold)
                    Text("How analysis works:", fontWeight = FontWeight.Medium)
                    Text("• Online mode: Your photo is sent to an AI service (Google Gemini via OpenRouter) for analysis. Data handling is governed by the provider's policy.")
                    Text("• Offline mode: Everything stays on your device — no data leaves your phone.")
                    Text("• Photo analysis requires internet connection. Educational content remains available on device.")
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("Your data:", fontWeight = FontWeight.Medium)
                    Text("• Analysis history is stored locally on your device only")
                    Text("• No analytics, tracking, or advertising")
                    Text("• No user accounts are created")
                    Text("• You can delete all data at any time from this settings screen")
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("Last updated: 2026", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) { Text("Close") }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About DermAware") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("DermAware", fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                    Text("Version ${BuildConfig.VERSION_NAME}")
                    Text("Skin Awareness for Everyone", fontWeight = FontWeight.Medium)
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("A humanitarian project providing free, offline skin health education to people worldwide who cannot access a dermatologist.",
                        style = MaterialTheme.typography.bodySmall)
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("⚠️ DISCLAIMER", fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium)
                    Text("This app is for educational awareness only. It is NOT a medical diagnostic tool. Always consult a qualified dermatologist for any skin concerns.",
                        style = MaterialTheme.typography.bodySmall)
                    Text("Category: Medical (Educational)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("Close") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Appearance section
            item {
                SettingsSectionHeader("Appearance")
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Translate,
                    title = "Language",
                    subtitle = viewModel.supportedLanguages.find { it.first == selectedLanguage }?.second
                        ?: "English",
                    onClick = { showLanguageDialog = true }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.DarkMode,
                    title = "Appearance",
                    subtitle = when (darkMode) {
                        "light" -> "Light Mode"
                        "dark" -> "Dark Mode"
                        else -> "System Default"
                    },
                    onClick = { showDarkModeDialog = true }
                )
            }

            // Notifications section
            item { SettingsSectionHeader("Notifications") }
            item {
                ListItem(
                    headlineContent = { Text("Monthly Skin Check Reminder") },
                    supportingContent = { Text("Get a reminder each month to check your skin") },
                    leadingContent = {
                        Icon(Icons.Default.Notifications, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingContent = {
                        Switch(
                            checked = monthlyReminderEnabled,
                            onCheckedChange = { viewModel.setMonthlyReminder(it) }
                        )
                    }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Privacy section
            item { SettingsSectionHeader("Privacy & Legal") }
            item {
                SettingsItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Privacy Policy",
                    subtitle = "How we handle your data",
                    onClick = { showPrivacyDialog = true }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "About DermAware",
                    subtitle = "Version ${BuildConfig.VERSION_NAME} • Disclaimer",
                    onClick = { showAboutDialog = true }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = "About Developer",
                    subtitle = "Saifullah Ahad • Contact & Hiring",
                    onClick = onNavigateToAboutDeveloper
                )
            }

            // Version footer at the bottom of settings
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "DermAware v${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp)
    )
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        trailingContent = {
            Icon(Icons.Default.ChevronRight, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        modifier = Modifier.clickableCompat(onClick = onClick)
    )
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}

// Extension to add clickable behavior to a Modifier
private fun Modifier.clickableCompat(onClick: () -> Unit): Modifier {
    return this.clickable(onClick = onClick)
}

@Composable
private fun LanguageSelectionDialog(
    currentLanguage: String,
    languages: List<Triple<String, String, String>>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Language") },
        text = {
            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                items(languages) { (code, englishName, nativeName) ->
                    ListItem(
                        headlineContent = { Text(nativeName) },
                        supportingContent = { if (nativeName != englishName) Text(englishName) },
                        leadingContent = {
                            RadioButton(
                                selected = currentLanguage == code,
                                onClick = { onSelect(code) }
                            )
                        },
                        modifier = Modifier.clickableCompat { onSelect(code) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
