package ai.saifullah.dermaware.ui.screen.analyze

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ai.saifullah.dermaware.ui.component.ConditionResultCard
import ai.saifullah.dermaware.ui.component.DisclaimerBanner

/**
 * Results screen — shows the top 3 matching conditions after photo analysis.
 * Shares the AnalyzeViewModel with AnalyzeScreen so it reads the analysis
 * results directly from memory without depending on a database round-trip.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConditionDetail: (String) -> Unit,
    onAnalyzeAnother: () -> Unit,
    viewModel: AnalyzeViewModel
) {
    val context = LocalContext.current
    // Read the analysis state from the shared ViewModel
    val analysisState by viewModel.analysisState.collectAsState()
    val currentSession = (analysisState as? AnalyzeViewModel.AnalysisState.Results)?.session

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text("Analysis Results") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (currentSession == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results to display")
                }
                return@Scaffold
            }

            // Analyzed photo and body area info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = currentSession.photoPath,
                    contentDescription = "Analyzed photo",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        text = "Body area: ${currentSession.bodyArea.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (currentSession.results.isNotEmpty()) {
                        Text(
                            text = "${currentSession.results.size} possible matches found",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = currentSession.errorMessage.ifEmpty { "No strong matches found" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Analysis engine indicator chip — shows whether Gemini or TFLite was used
            AnalysisEngineChip(isOnlineAnalysis = currentSession.isOnlineAnalysis)

            // Fallback info banner — shown when Gemini failed and TFLite was used instead
            if (currentSession.fallbackMessage.isNotBlank()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = currentSession.fallbackMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            // AI summary card — only shown when Gemini provided a text observation
            if (currentSession.aiSummary.isNotBlank()) {
                AiSummaryCard(summary = currentSession.aiSummary)
            }

            // Results list or no-match message
            if (currentSession.results.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.SearchOff, contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("No confident match found",
                            style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Try retaking the photo with better lighting and closer to the affected area. For persistent concerns, consult a dermatologist.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Text(
                    text = "Top Matches",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Tap any result to learn more",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                currentSession.results.forEachIndexed { index, result ->
                    ConditionResultCard(
                        result = result,
                        rank = index + 1,
                        onClick = { onNavigateToConditionDetail(result.conditionId) }
                    )
                }
            }

            // Prominent disclaimer banner — required on every result screen
            DisclaimerBanner()

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onAnalyzeAnother,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Replay, contentDescription = null,
                        modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("New Analysis")
                }
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Done")
                }
            }

            // Share button — lets the user share the results summary as text
            if (currentSession.results.isNotEmpty()) {
                OutlinedButton(
                    onClick = {
                        // Build a text summary of the analysis results
                        val resultsText = currentSession.results.joinToString("\n") { result ->
                            "- ${result.displayName} (${(result.confidence * 100).toInt()}% match)"
                        }
                        val shareText = buildString {
                            appendLine("DermAware Skin Analysis Results")
                            appendLine("Body area: ${currentSession.bodyArea.replaceFirstChar { it.uppercase() }}")
                            appendLine()
                            appendLine("Top matches:")
                            appendLine(resultsText)
                            appendLine()
                            appendLine("DISCLAIMER: This is for educational awareness only — NOT a medical diagnosis. Always consult a qualified dermatologist for any skin concerns.")
                        }
                        // Open the Android share sheet
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Results"))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Share, contentDescription = null,
                        modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Share Results")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Small chip showing which analysis engine was used.
 * "AI-powered" for Gemini online analysis, "Offline analysis" for TFLite on-device.
 */
@Composable
private fun AnalysisEngineChip(isOnlineAnalysis: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isOnlineAnalysis)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (isOnlineAnalysis) Icons.Default.Cloud else Icons.Default.PhoneAndroid,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isOnlineAnalysis)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (isOnlineAnalysis) "AI-powered analysis" else "Offline analysis",
                style = MaterialTheme.typography.labelSmall,
                color = if (isOnlineAnalysis)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Card showing Gemini's educational observation about the skin photo.
 * Only displayed when Gemini was used and returned a summary.
 */
@Composable
private fun AiSummaryCard(summary: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "AI Observation",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
