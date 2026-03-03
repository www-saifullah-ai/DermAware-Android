package ai.saifullah.dermaware.data.model

import ai.saifullah.dermaware.data.model.UrgencyLevel

/**
 * Represents a skin condition in the offline information library.
 * All 50+ conditions are stored as Kotlin data objects — no database needed,
 * no internet required. This is all pre-bundled with the app.
 */
data class SkinCondition(
    // Unique ID matching the TFLite model label (e.g., "tinea_corporis")
    val id: String,

    // Display name in English (e.g., "Ringworm (Tinea Corporis)")
    val name: String,

    // Category (e.g., "Infection", "Inflammatory", "Pigmentation", "Dangerous", "Other")
    val category: String,

    // Simple plain-language description of what this condition is
    val description: String,

    // List of common symptoms (bullet points on the condition detail screen)
    val symptoms: List<String>,

    // Who is commonly affected by this condition
    val whoIsAffected: String,

    // Body areas this condition typically appears on
    val affectedAreas: List<String>,

    // Home care tips — what to do
    val homeCareSteps: List<String>,

    // Things to avoid that make it worse
    val thingsToAvoid: List<String>,

    // Over-the-counter treatment categories (NOT brand names)
    val otcTreatmentCategories: List<String>,

    // When to see a doctor — urgency classification
    val urgencyLevel: UrgencyLevel,

    // Specific warning signs that require immediate medical attention
    val emergencyWarnings: List<String>,

    // What type of doctor to see (e.g., "Dermatologist", "General Practitioner")
    val doctorType: String,

    // Whether this condition can spread to other people
    val isContagious: Boolean,

    // Whether this condition is a potentially dangerous/serious condition
    val isDangerous: Boolean,

    // Search tags for the library search feature
    val searchTags: List<String>
)
