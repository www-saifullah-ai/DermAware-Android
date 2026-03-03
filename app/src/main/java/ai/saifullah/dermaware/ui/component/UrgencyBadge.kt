package ai.saifullah.dermaware.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ai.saifullah.dermaware.data.model.UrgencyLevel

/**
 * Colored badge showing the urgency level for a skin condition.
 * Used in the condition detail screen to show how soon to see a doctor.
 */
@Composable
fun UrgencyBadge(urgencyLevel: UrgencyLevel, modifier: Modifier = Modifier) {
    val (backgroundColor, textColor, emoji, label) = when (urgencyLevel) {
        UrgencyLevel.LOW -> Quadruple(
            Color(0xFF4CAF50).copy(alpha = 0.15f),
            Color(0xFF2E7D32),
            "🟢",
            "LOW — Monitor at home"
        )
        UrgencyLevel.MEDIUM -> Quadruple(
            Color(0xFFFF9800).copy(alpha = 0.15f),
            Color(0xFFE65100),
            "🟡",
            "MEDIUM — See a doctor this week"
        )
        UrgencyLevel.HIGH -> Quadruple(
            Color(0xFFF44336).copy(alpha = 0.15f),
            Color(0xFFC62828),
            "🔴",
            "HIGH — See a doctor within 1-2 days"
        )
        UrgencyLevel.EMERGENCY -> Quadruple(
            Color(0xFF9C27B0).copy(alpha = 0.15f),
            Color(0xFF6A1B9A),
            "🚨",
            "EMERGENCY — Seek immediate care"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Simple data holder — Kotlin doesn't have a built-in Quadruple
 */
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
