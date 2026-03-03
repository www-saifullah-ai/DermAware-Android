package ai.saifullah.dermaware.ui.screen.aboutdeveloper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ai.saifullah.dermaware.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var showRecruiterSection by remember { mutableStateOf(false) }
    var showPhilosophySection by remember { mutableStateOf(false) }
    var showPersonalNote by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text("About Developer") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Hero card
            item { HeroCard() }

            // Contact section
            item { ContactSection(context) }

            // Hiring links
            item { HiringSection(context) }

            // Social links
            item { SocialLinksSection(context) }

            // Stats
            item { StatsSection() }

            // Philosophy (collapsible)
            item {
                CollapsibleSection(
                    title = "Why I build free apps",
                    expanded = showPhilosophySection,
                    onToggle = { showPhilosophySection = !showPhilosophySection }
                ) {
                    PhilosophyContent()
                }
            }

            // Recruiter section (collapsible)
            item {
                CollapsibleSection(
                    title = "For recruiters",
                    expanded = showRecruiterSection,
                    onToggle = { showRecruiterSection = !showRecruiterSection }
                ) {
                    RecruiterContent(context)
                }
            }

            // Personal note (collapsible)
            item {
                CollapsibleSection(
                    title = "Personal note",
                    expanded = showPersonalNote,
                    onToggle = { showPersonalNote = !showPersonalNote }
                ) {
                    Text(
                        text = "\u201CThe blessings flow through me; I simply distribute them.\u201D",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

// ── Hero Card ────────────────────────────────────────────────────────────────

@Composable
private fun HeroCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar
            Image(
                painter = painterResource(R.drawable.dev_avatar),
                contentDescription = "Developer profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )

            // Name
            Text(
                text = "Saifullah Ahad",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Role
            Text(
                text = "Independent Software Creator",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Dhaka, Bangladesh",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Available for Hire badge
            Surface(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Available for Hire",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Tagline
            Text(
                text = "Building free, privacy-respecting apps that stay reliable over time.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

// ── Contact Section ──────────────────────────────────────────────────────────

@Composable
private fun ContactSection(context: Context) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader("Contact")

        // Primary contact buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ContactButton(
                icon = Icons.Default.Email,
                label = "Email",
                modifier = Modifier.weight(1f),
                onClick = {
                    openUrl(context, "mailto:www.saifullah.ai@gmail.com?subject=Hiring%20%2F%20Collaboration")
                }
            )
            ContactButton(
                icon = Icons.Default.Chat,
                label = "WhatsApp",
                modifier = Modifier.weight(1f),
                onClick = { openUrl(context, "https://wa.me/8801711134346") }
            )
            ContactButton(
                icon = Icons.Default.Call,
                label = "Call",
                modifier = Modifier.weight(1f),
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+8801711134346"))
                    context.startActivity(intent)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Phone number with copy
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .clickable { copyToClipboard(context, "+8801711134346", "Phone number") }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "+8801711134346",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(
                Icons.Default.ContentCopy,
                contentDescription = "Copy phone number",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Email with copy
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .clickable { copyToClipboard(context, "www.saifullah.ai@gmail.com", "Email") }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "www.saifullah.ai@gmail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.Default.ContentCopy,
                contentDescription = "Copy email",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Helper line
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "For hiring/business inquiries. WhatsApp is best if you can't call.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ── Hiring Section ───────────────────────────────────────────────────────────

@Composable
private fun HiringSection(context: Context) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader("Hiring")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { openUrl(context, "https://hire.saifullah.ai") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Work, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Hire Profile", maxLines = 1)
            }
            OutlinedButton(
                onClick = { openUrl(context, "https://resume.saifullah.ai") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Resume", maxLines = 1)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedButton(
            onClick = { openUrl(context, "https://saifullah.ai") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Portfolio — saifullah.ai")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ── Social Links ─────────────────────────────────────────────────────────────

private data class SocialLink(
    val icon: ImageVector,
    val label: String,
    val url: String
)

@Composable
private fun SocialLinksSection(context: Context) {
    val links = listOf(
        SocialLink(Icons.Default.Apps, "All Apps", "https://privacy.saifullah.ai"),
        SocialLink(Icons.Default.Person, "LinkedIn", "https://www.linkedin.com/in/www-saifullah-"),
        SocialLink(Icons.Default.Code, "GitHub", "https://github.com/www-saifullah-"),
        SocialLink(Icons.Default.ShoppingCart, "Play Store", "https://play.google.com/store/apps/developer?id=SAIFULLAH"),
        SocialLink(Icons.Default.PlayArrow, "YouTube", "https://www.youtube.com/@AI.SAIFULLAH"),
        SocialLink(Icons.Default.Tag, "X", "https://x.com/saifullah_ai")
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader("Social")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(links) { link ->
                AssistChip(
                    onClick = { openUrl(context, link.url) },
                    label = { Text(link.label) },
                    leadingIcon = {
                        Icon(
                            link.icon,
                            contentDescription = link.label,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ── Stats Section ────────────────────────────────────────────────────────────

@Composable
private fun StatsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionHeader("Stats")
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // App counts row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("42", "Total Apps")
                    StatItem("38", "Android")
                    StatItem("4", "Desktop")
                    StatItem("4", "Platforms")
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // Other stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Translate,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("30+ languages supported", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Free Forever \u2014 no subscriptions, no paywalls, no premium tiers",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ── Philosophy Section ───────────────────────────────────────────────────────

@Composable
private fun PhilosophyContent() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Software Is a Natural Resource",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Like oxygen, water, and sunlight \u2014 software should be essential, abundant, and freely available to all.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "\u201CYou don\u2019t need to be a professional to produce professional results.\u201D",
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ── Recruiter Section ────────────────────────────────────────────────────────

@Composable
private fun RecruiterContent(context: Context) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Target roles
        Text(
            text = "Target roles",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        listOf(
            "Software Product Lead",
            "AI-Enabled Product Manager",
            "Product Systems Designer",
            "Digital Product Creator"
        ).forEach { role ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = role, style = MaterialTheme.typography.bodyMedium)
            }
        }

        HorizontalDivider()

        // Work preferences
        Text(
            text = "Work preferences & availability",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        val preferences = listOf(
            "Preferred" to "Remote / Work From Home",
            "On-site" to "Open to occasional visits (1\u20132\u00D7/month)",
            "Type" to "Full-time or Freelance",
            "Availability" to "Immediate \u2014 ready upon visa sponsorship",
            "Relocation" to "Ready to relocate (with family)",
            "Visa" to "Requires visa sponsorship"
        )
        preferences.forEach { (key, value) ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "$key:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        HorizontalDivider()

        // Preferred regions
        Text(
            text = "Preferred regions",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "UAE \u2022 Qatar \u2022 Saudi Arabia \u2022 Malaysia \u2022 Turkey",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider()

        // Recruiter CTA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = {
                    openUrl(context, "mailto:www.saifullah.ai@gmail.com?subject=Hiring%20%2F%20Collaboration")
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Email", maxLines = 1)
            }
            FilledTonalButton(
                onClick = { openUrl(context, "https://wa.me/8801711134346") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("WhatsApp", maxLines = 1)
            }
        }
        FilledTonalButton(
            onClick = { openUrl(context, "https://hire.saifullah.ai") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Work, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Hire Profile")
        }
    }
}

// ── Shared Components ────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ContactButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, maxLines = 1)
    }
}

@Composable
private fun CollapsibleSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        AnimatedVisibility(visible = expanded) {
            content()
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// ── Utility Functions ────────────────────────────────────────────────────────

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

private fun copyToClipboard(context: Context, text: String, label: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    Toast.makeText(context, "$label copied", Toast.LENGTH_SHORT).show()
}
