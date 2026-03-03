package ai.saifullah.dermaware.ui.screen.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ai.saifullah.dermaware.data.model.SkinCondition
import ai.saifullah.dermaware.data.model.UrgencyLevel
import ai.saifullah.dermaware.domain.SkinConditionDatabase
import ai.saifullah.dermaware.ui.component.DisclaimerBanner
import ai.saifullah.dermaware.ui.component.UrgencyBadge

/**
 * Condition detail screen — shows complete information about a single skin condition.
 * This is the most information-dense screen in the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConditionDetailScreen(
    conditionId: String,
    onNavigateBack: () -> Unit
) {
    val condition = SkinConditionDatabase.getConditionById(conditionId)

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(condition?.name ?: "Condition Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (condition == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Error, contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Text("Condition not found")
                    TextButton(onClick = onNavigateBack) { Text("Go Back") }
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dangerous condition warning banner
            if (condition.isDangerous) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null,
                            tint = MaterialTheme.colorScheme.error)
                        Column {
                            Text("Important Awareness Condition",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer)
                            Text("If you see warning signs, see a dermatologist promptly.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            // Category and tags
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = { },
                    label = { Text(condition.category) },
                    leadingIcon = {
                        Icon(Icons.Default.Label, contentDescription = null,
                            modifier = Modifier.size(16.dp))
                    }
                )
                if (condition.isContagious) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Contagious") },
                        leadingIcon = {
                            Icon(Icons.Default.Warning, contentDescription = null,
                                modifier = Modifier.size(16.dp))
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            // Urgency level
            UrgencyBadge(urgencyLevel = condition.urgencyLevel, modifier = Modifier.fillMaxWidth())

            // Description
            SectionCard(title = "What is it?", icon = Icons.Default.Info) {
                Text(condition.description, style = MaterialTheme.typography.bodyMedium,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight)
            }

            // Symptoms
            SectionCard(title = "Common Symptoms", icon = Icons.Default.Sick) {
                condition.symptoms.forEach { symptom ->
                    BulletPoint(text = symptom)
                }
            }

            // Who is affected
            SectionCard(title = "Who Is Affected", icon = Icons.Default.People) {
                Text(condition.whoIsAffected, style = MaterialTheme.typography.bodyMedium)
            }

            // Affected body areas
            SectionCard(title = "Common Body Areas", icon = Icons.Default.Person) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    condition.affectedAreas.forEach { area ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text(area, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                        )
                    }
                }
            }

            // Home care steps
            SectionCard(
                title = "Home Care",
                icon = Icons.Default.Home,
                subtitleText = "General Information Only — Not Medical Advice"
            ) {
                condition.homeCareSteps.forEach { step ->
                    BulletPoint(text = step)
                }
            }

            // Things to avoid
            SectionCard(title = "What to Avoid", icon = Icons.Default.Block) {
                condition.thingsToAvoid.forEach { item ->
                    BulletPoint(text = item, bulletColor = MaterialTheme.colorScheme.error)
                }
            }

            // OTC treatment categories
            if (condition.otcTreatmentCategories.isNotEmpty()) {
                SectionCard(title = "General Treatment Categories",
                    icon = Icons.Default.LocalPharmacy,
                    subtitleText = "Consult a pharmacist or doctor before use") {
                    condition.otcTreatmentCategories.forEach { category ->
                        BulletPoint(text = category)
                    }
                }
            }

            // Emergency warnings
            if (condition.emergencyWarnings.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp))
                            Text("When to Seek Medical Help",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                        condition.emergencyWarnings.forEach { warning ->
                            BulletPoint(text = warning,
                                bulletColor = MaterialTheme.colorScheme.error,
                                textColor = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            // Doctor referral info
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.LocalHospital, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Recommended Doctor",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold)
                        Text(condition.doctorType,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Bottom disclaimer
            DisclaimerBanner()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    subtitleText: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp))
                Column {
                    Text(title, style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold)
                    if (subtitleText != null) {
                        Text(subtitleText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            content()
        }
    }
}

@Composable
private fun BulletPoint(
    text: String,
    bulletColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(50))
                .background(bulletColor)
                .padding(top = 7.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
            modifier = Modifier.weight(1f)
        )
    }
}
