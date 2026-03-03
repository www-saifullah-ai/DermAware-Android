package ai.saifullah.dermaware.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Small persistent disclaimer shown at the bottom of every analysis result screen.
 * Google Play Store requires this to be visible on all result screens.
 */
@Composable
fun DisclaimerBanner(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp).padding(top = 2.dp)
            )
            Text(
                text = "This is for educational awareness only. NOT a medical diagnosis. Always consult a qualified dermatologist for proper evaluation.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * Full-screen disclaimer dialog shown on first app launch.
 * User MUST tap "I Understand" to proceed — cannot be dismissed otherwise.
 */
@Composable
fun FullScreenDisclaimerDialog(onAcknowledge: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* Cannot dismiss — user must tap button */ },
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Important Health Notice",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "DermAware is an educational tool designed to provide general skin health information and awareness.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider()
                Text(
                    text = "⚠️ It is NOT a substitute for professional medical advice, diagnosis, or treatment.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error
                )
                Divider()
                Text(
                    text = "Always consult a qualified dermatologist or healthcare provider for any skin concerns.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "In case of emergency, contact emergency services immediately.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onAcknowledge,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("I Understand — Continue")
            }
        }
    )
}
