package ai.saifullah.dermaware.ui.screen.symptom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ai.saifullah.dermaware.ui.component.ConditionResultCard
import ai.saifullah.dermaware.ui.component.DisclaimerBanner

/**
 * Symptom Checker screen — a step-by-step text questionnaire.
 * User answers 6 questions about their symptoms, then sees matching conditions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomCheckerScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConditionDetail: (String) -> Unit,
    viewModel: SymptomViewModel = hiltViewModel()
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val isComplete by viewModel.isComplete.collectAsState()
    val results by viewModel.results.collectAsState()
    val totalSteps = viewModel.totalSteps

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text("Symptom Checker") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 0 && !isComplete) {
                            viewModel.previousStep()
                        } else {
                            viewModel.reset()
                            onNavigateBack()
                        }
                    }) {
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
        ) {
            // Progress bar
            if (!isComplete) {
                LinearProgressIndicator(
                    progress = { (currentStep + 1).toFloat() / totalSteps.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Question ${currentStep + 1} of $totalSteps",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            if (isComplete) {
                // Results view
                SymptomResultsView(
                    results = results,
                    onNavigateToConditionDetail = onNavigateToConditionDetail,
                    onStartOver = {
                        viewModel.reset()
                    }
                )
            } else {
                // Question view
                val question = viewModel.questions[currentStep]
                var selectedAnswer by remember(currentStep) { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Question text
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // Answer options as selectable cards
                    question.options.forEach { option ->
                        val isSelected = selectedAnswer == option
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            border = if (isSelected)
                                CardDefaults.outlinedCardBorder()
                            else null,
                            onClick = { selectedAnswer = option }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedAnswer = option }
                                )
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Next button
                    Button(
                        onClick = {
                            if (selectedAnswer.isNotEmpty()) {
                                viewModel.answerQuestion(question.key, selectedAnswer)
                                viewModel.nextStep()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedAnswer.isNotEmpty(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (currentStep < totalSteps - 1) "Next" else "See Results",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            if (currentStep < totalSteps - 1) Icons.Default.ArrowForward
                            else Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SymptomResultsView(
    results: List<ai.saifullah.dermaware.data.model.ConditionResult>,
    onNavigateToConditionDetail: (String) -> Unit,
    onStartOver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Possible Matches",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Based on your answers, these conditions may be relevant. Tap any for detailed information.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (results.isEmpty()) {
            Card(colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )) {
                Column(modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No strong matches found for your combination of symptoms.")
                    Spacer(Modifier.height(8.dp))
                    Text("Please consult a dermatologist for proper evaluation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            results.forEachIndexed { index, result ->
                ConditionResultCard(
                    result = result,
                    rank = index + 1,
                    onClick = { onNavigateToConditionDetail(result.conditionId) }
                )
            }
        }

        DisclaimerBanner()

        OutlinedButton(
            onClick = onStartOver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Start Over")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
