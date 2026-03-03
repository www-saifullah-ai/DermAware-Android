package ai.saifullah.dermaware.ui.screen.education

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Education screen — offline articles about skin health awareness.
 * All content is pre-bundled, no internet required.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(
    onNavigateBack: () -> Unit
) {
    var selectedArticle by remember { mutableStateOf<EducationArticle?>(null) }

    val articles = remember {
        listOf(
            EducationArticle(
                id = "monthly_self_check",
                icon = Icons.Default.Search,
                title = "Monthly Skin Self-Check",
                subtitle = "How to check your skin for changes",
                content = """
**Why Do a Monthly Skin Check?**

Early detection of skin cancer dramatically improves outcomes. Melanoma detected early has over 98% survival rate; detected late, it drops significantly.

**What You Need:**
• A full-length mirror
• A hand mirror
• Good lighting
• 10 minutes once a month

**How to Do It:**

1. **Face and Scalp** — Check face, lips, ears, and scalp (use a comb to part hair and check scalp)

2. **Neck and Chest** — Check front and back of neck. Check chest and upper back using mirrors

3. **Arms** — Check all sides of arms, including armpits and between fingers

4. **Torso** — Use hand mirror to check back and buttocks

5. **Legs** — Check front and back of legs, including behind knees, ankles, between toes, soles of feet

6. **Nails** — Check under fingernails and toenails for any dark streaks

**What to Look For (ABCDE Rule):**
- **A** — Asymmetry
- **B** — Border irregularity
- **C** — Color variation
- **D** — Diameter > 6mm
- **E** — Evolving/changing

**When to See a Doctor:**
If you find anything that concerns you, especially anything following the ABCDE criteria, see a dermatologist. Better safe than sorry.
                """.trimIndent()
            ),
            EducationArticle(
                id = "abcde_rule",
                icon = Icons.Default.Warning,
                title = "The ABCDE Rule for Moles",
                subtitle = "Melanoma warning signs to know",
                content = """
**What Is the ABCDE Rule?**

The ABCDE rule helps you identify warning signs in moles that may indicate melanoma, the most dangerous form of skin cancer.

**A — Asymmetry**
One half of the mole does not match the other half. Normal moles are usually round and symmetrical.

**B — Border**
The edges are irregular, ragged, notched, or blurred. Normal moles have smooth, well-defined borders.

**C — Color**
The color is not uniform. There may be different shades of brown, black, pink, red, white, or blue within the same mole.

**D — Diameter**
The mole is larger than 6mm across — about the size of a pencil eraser. Note: melanomas can be smaller.

**E — Evolving**
The mole is changing in size, shape, color, or is developing a new symptom like bleeding, itching, or crusting.

**Important:**
Any single ABCDE feature is enough reason to see a dermatologist. Do not wait to see if it "goes away."

**Normal Moles:**
• Same color throughout (tan, brown)
• Symmetrical shape
• Well-defined borders
• Stable — not changing
• Smooth surface
                """.trimIndent()
            ),
            EducationArticle(
                id = "sun_protection",
                icon = Icons.Default.WbSunny,
                title = "Sun Protection Basics",
                subtitle = "UV awareness and skin protection",
                content = """
**Why Sun Protection Matters**

UV radiation from the sun is the primary cause of most skin cancers and accelerates skin aging. Protection is simple but must be consistent.

**Understanding UV Radiation:**
• **UVA** — Ages skin, penetrates deep, present all day year-round
• **UVB** — Burns skin, causes most skin cancers
• Both types can penetrate clouds — UV exists even on cloudy days

**Choosing a Sunscreen:**
• SPF 30+ for daily use; SPF 50+ for outdoor activities
• Choose "broad-spectrum" — protects against both UVA and UVB
• Water-resistant formula for swimming or sport

**How to Apply Sunscreen:**
• Apply 15-20 minutes before going outside
• Use enough — about 1 teaspoon for face and neck
• Reapply every 2 hours (or after swimming/sweating)
• Apply to all exposed skin — don't forget ears, neck, and top of feet

**Additional Protection:**
• Wear a wide-brimmed hat
• Wear UV-protective clothing (UPF rated)
• Seek shade between 10am and 4pm
• Never use tanning beds

**Who Needs It:**
Everyone, regardless of skin tone. Darker skin has more natural protection but can still burn and develop skin cancer.
                """.trimIndent()
            ),
            EducationArticle(
                id = "skin_hygiene",
                icon = Icons.Default.CleanHands,
                title = "Skin Hygiene & Daily Care",
                subtitle = "Caring for your skin in any climate",
                content = """
**Basic Skin Hygiene**

Good skin hygiene prevents many infections and keeps your skin barrier healthy.

**Daily Face Care:**
• Wash face twice daily — morning and evening
• Use lukewarm water (not hot — strips natural oils)
• Gentle, pH-balanced cleanser for your skin type
• Pat dry with a clean towel — don't rub
• Apply moisturizer while skin is still slightly damp

**Body Care:**
• Shower daily in hot or humid climates (every 2 days in cold climates is acceptable)
• Wash thoroughly in skin folds — underarms, groin, under breasts
• Dry skin completely after bathing, especially between toes
• Apply body moisturizer to prevent dryness and itching

**In Hot, Humid Climates:**
• Shower after sweating to prevent fungal infections
• Wear loose, breathable cotton clothing
• Change clothes daily (or twice daily if very hot)
• Keep skin folds dry with talc powder or antifungal powder

**In Cold, Dry Climates:**
• Increase moisturizer frequency — morning and evening
• Use a humidifier indoors
• Avoid very hot showers
• Wear gloves outdoors to protect hands

**Feet Care:**
• Wash between toes daily and dry thoroughly
• Wear footwear in public showers
• Change socks daily
• Trim toenails straight across
                """.trimIndent()
            ),
            EducationArticle(
                id = "skin_children",
                icon = Icons.Default.ChildCare,
                title = "Skin Care for Children",
                subtitle = "Keeping children's skin healthy",
                content = """
**Children's Skin Is Different**

Children's skin is thinner, more sensitive, and more permeable than adult skin. Products that are safe for adults may irritate children.

**Newborn & Baby Skin:**
• Newborn skin is naturally dry and may peel — this is normal
• Use only gentle, fragrance-free, hypoallergenic products
• Avoid strong soaps — plain warm water is sufficient for newborns
• Cradle cap (yellow flaky patches on scalp) — normal and temporary, apply baby oil and gently comb
• Nappy/diaper rash — change diapers frequently, apply barrier cream, allow air time

**Sun Protection for Children:**
• Babies under 6 months: keep out of direct sun; use clothing and shade rather than sunscreen
• Children over 6 months: apply SPF 30+ broad-spectrum sunscreen on all exposed skin
• Children burn faster than adults — be more vigilant

**Eczema in Children:**
• Very common — affects 1 in 5 children
• Use fragrance-free products only
• Bathe in lukewarm water for max 10 minutes
• Moisturize immediately after bath
• Keep fingernails short to reduce skin damage from scratching

**Preventing Infections:**
• Teach children to wash hands frequently
• Don't share towels or combs
• Treat any cuts or wounds promptly
• Check for head lice and treat promptly
                """.trimIndent()
            )
        )
    }

    if (selectedArticle != null) {
        // Article detail view
        ArticleDetailView(
            article = selectedArticle!!,
            onBack = { selectedArticle = null }
        )
    } else {
        // Article list view
        Scaffold(
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(0.dp),
                    title = { Text("Skin Education") },
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
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Learn about skin health, self-checks, and prevention.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(articles, key = { it.id }) { article ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { selectedArticle = article },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(article.icon, contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(article.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold)
                                Text(article.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleDetailView(
    article: EducationArticle,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(article.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                // Simple markdown-like rendering for the article content
                Text(
                    text = article.content,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class EducationArticle(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val content: String
)
