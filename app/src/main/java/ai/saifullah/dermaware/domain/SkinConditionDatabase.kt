package ai.saifullah.dermaware.domain

import ai.saifullah.dermaware.data.model.SkinCondition
import ai.saifullah.dermaware.data.model.UrgencyLevel

/**
 * Offline database of 50+ skin conditions.
 *
 * ALL information is stored here — no internet required.
 * This data is bundled with the app at install time.
 *
 * IMPORTANT: All content is for educational awareness only.
 * This is NOT medical advice. Users must consult a qualified
 * dermatologist for proper diagnosis and treatment.
 */
object SkinConditionDatabase {

    /**
     * Returns all conditions as a list.
     * Used by the Library screen to display browsable condition cards.
     */
    fun getAllConditions(): List<SkinCondition> = listOf(
        // ─── INFECTIONS ──────────────────────────────────────────────────────────

        SkinCondition(
            id = "tinea_corporis",
            name = "Ringworm (Tinea Corporis)",
            category = "Infection",
            description = "Ringworm is a common fungal infection of the skin. Despite its name, it has nothing to do with worms. It appears as a red, circular, scaly ring on the skin. It is caused by a fungus called dermatophyte that lives on the outer layer of skin.",
            symptoms = listOf(
                "Ring-shaped red or pink rash with a clearer center",
                "Scaly, itchy, raised border",
                "Rash may slowly expand outward",
                "Mild burning or discomfort",
                "Affected area may have hair loss (if on scalp)"
            ),
            whoIsAffected = "Anyone can get ringworm, but children and people who sweat heavily, use public showers/gyms, or are in close contact with infected animals are at higher risk.",
            affectedAreas = listOf("Arms", "Legs", "Trunk", "Scalp", "Face", "Groin"),
            homeCareSteps = listOf(
                "Keep the affected area clean and dry",
                "Wash bedding, clothing, and towels regularly in hot water",
                "Do not share personal items (towels, combs, clothing)",
                "Apply antifungal cream from a pharmacy as directed",
                "Avoid scratching to prevent spreading",
                "Continue treatment for 2-4 weeks even if symptoms improve"
            ),
            thingsToAvoid = listOf(
                "Sharing towels, clothing, or combs",
                "Walking barefoot in locker rooms or public showers",
                "Tight-fitting clothing that traps moisture",
                "Contact sports until treated"
            ),
            otcTreatmentCategories = listOf(
                "Antifungal creams (clotrimazole, miconazole, or terbinafine based)",
                "Antifungal powders for feet/body areas"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Rash spreads rapidly despite treatment",
                "Signs of bacterial infection: warmth, pus, increasing pain",
                "Scalp involvement with swelling or pus — needs prescription antifungal"
            ),
            doctorType = "General Practitioner or Dermatologist",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("ringworm", "fungal", "circular rash", "tinea", "itchy ring", "fungus")
        ),

        SkinCondition(
            id = "tinea_pedis",
            name = "Athlete's Foot (Tinea Pedis)",
            category = "Infection",
            description = "Athlete's foot is a fungal infection that mainly affects the skin between the toes and on the soles of the feet. It causes itching, burning, and peeling skin. It spreads easily in warm, damp places like locker rooms and swimming pools.",
            symptoms = listOf(
                "Itching, stinging, and burning between toes",
                "Itchy, blistering, peeling rash",
                "Dry, flaky skin on the soles and sides of feet",
                "Raw, cracked skin — especially between toes",
                "Toenails that are discolored, thick, or crumbling (if infection spreads)"
            ),
            whoIsAffected = "Common in athletes and people who wear tight shoes or keep feet damp. Anyone who uses shared showers or pools can get it.",
            affectedAreas = listOf("Between toes", "Soles of feet", "Sides of feet"),
            homeCareSteps = listOf(
                "Wash feet daily with soap and water, dry thoroughly — especially between toes",
                "Apply antifungal cream or powder to affected areas",
                "Wear clean cotton socks and change them daily",
                "Let feet air out when possible — wear open-toed shoes at home",
                "Use footwear in public showers, pools, and gyms",
                "Do not walk barefoot on shared surfaces"
            ),
            thingsToAvoid = listOf(
                "Tight, non-breathable shoes",
                "Wearing the same shoes every day without airing them out",
                "Synthetic socks that trap moisture",
                "Barefoot in public wet areas"
            ),
            otcTreatmentCategories = listOf(
                "Antifungal creams (miconazole or terbinafine based)",
                "Antifungal foot powders or sprays"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Signs of bacterial infection: red streaks, swelling, pus, fever",
                "Diabetic patients: any foot infection requires immediate medical attention"
            ),
            doctorType = "General Practitioner or Dermatologist",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("athlete's foot", "foot fungus", "itchy feet", "peeling feet", "tinea pedis")
        ),

        SkinCondition(
            id = "tinea_capitis",
            name = "Scalp Ringworm (Tinea Capitis)",
            category = "Infection",
            description = "Tinea capitis is a fungal infection of the scalp and hair. It is most common in young children. It can cause scaly, itchy patches on the scalp and may lead to hair loss in affected areas. This type usually requires prescription antifungal medication taken by mouth.",
            symptoms = listOf(
                "Round, scaly, itchy patches on the scalp",
                "Brittle hair that breaks easily near the scalp",
                "Hair loss in patchy areas",
                "Black dots on scalp where hair has broken off",
                "Swollen, tender lymph nodes in the neck",
                "Kerion: a painful, swollen, pus-filled lump (severe form)"
            ),
            whoIsAffected = "Most common in children ages 2-10. Rare in adults. Spreads in schools, daycares, and through sharing combs, hats, or pillows.",
            affectedAreas = listOf("Scalp", "Eyebrows", "Eyelashes"),
            homeCareSteps = listOf(
                "See a doctor — scalp ringworm usually requires oral antifungal medicine",
                "Use antifungal shampoo as advised by a doctor to reduce spreading",
                "Wash hats, scarves, pillowcases, and combs regularly",
                "Do not share combs, brushes, hats, or pillows",
                "Continue treatment for the full prescribed duration"
            ),
            thingsToAvoid = listOf(
                "Sharing hair accessories, hats, or pillows",
                "Stopping treatment early when hair starts growing back"
            ),
            otcTreatmentCategories = listOf(
                "Antifungal shampoos can reduce spread but are NOT sufficient alone",
                "Oral antifungal medications are required — see a doctor"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Kerion (large pus-filled swelling on scalp) — see doctor urgently",
                "Fever with scalp swelling",
                "Rapid hair loss over large area"
            ),
            doctorType = "Dermatologist or Pediatrician",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("scalp fungus", "hair loss", "scalp ringworm", "tinea capitis", "children fungus")
        ),

        SkinCondition(
            id = "tinea_unguium",
            name = "Nail Fungus (Onychomycosis)",
            category = "Infection",
            description = "Nail fungus is a common infection that causes nails to become discolored, thick, brittle, and misshapen. It usually starts at the tip of the nail and gradually spreads. Toenails are much more commonly affected than fingernails. Treatment is slow and can take months.",
            symptoms = listOf(
                "Yellowed, brown, or white discoloration of the nail",
                "Thickened, brittle, or crumbling nail",
                "Nail separated from the nail bed",
                "Distorted nail shape",
                "Foul odor from the nail",
                "Slight pain or discomfort in severe cases"
            ),
            whoIsAffected = "More common in older adults, diabetics, people with poor circulation, athletes using communal showers, and those with a history of athlete's foot.",
            affectedAreas = listOf("Toenails", "Fingernails"),
            homeCareSteps = listOf(
                "Keep nails trimmed short and file down thick nails",
                "Keep feet clean and dry",
                "Wear moisture-wicking socks and breathable shoes",
                "Use antifungal nail treatments (takes 6-12 months)",
                "Wear footwear in communal showers and around pools",
                "Do not share nail clippers or files"
            ),
            thingsToAvoid = listOf(
                "Tight shoes that press on toenails",
                "Keeping feet wet for long periods",
                "Nail polish over infected nails (traps moisture)"
            ),
            otcTreatmentCategories = listOf(
                "Antifungal nail lacquers/polish",
                "Topical antifungal solutions for nails"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Diabetic patients: nail infections can lead to serious complications — see a doctor promptly",
                "Signs of spreading infection: pain, swelling, redness around nail"
            ),
            doctorType = "Dermatologist or General Practitioner",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("nail fungus", "yellow nails", "thick nails", "onychomycosis", "nail infection")
        ),

        SkinCondition(
            id = "scabies",
            name = "Scabies",
            category = "Infection",
            description = "Scabies is a very itchy skin condition caused by tiny mites (Sarcoptes scabiei) that burrow into the skin. The intense itching is caused by an allergic reaction to the mites. It spreads through prolonged skin-to-skin contact and requires prescription treatment for all household members.",
            symptoms = listOf(
                "Intense itching — much worse at night",
                "Thin, irregular burrow tracks (fine wavy lines) in the skin",
                "Pimple-like rash",
                "Small blisters or scales",
                "Sores caused by scratching",
                "Common sites: between fingers, wrists, armpits, waist, inner elbows, buttocks"
            ),
            whoIsAffected = "Anyone can get scabies. It spreads easily in close living conditions — families, nursing homes, schools. Sexual contact is also a route of transmission.",
            affectedAreas = listOf("Between fingers", "Wrists", "Armpits", "Waist", "Inner elbows", "Buttocks", "Genitals"),
            homeCareSteps = listOf(
                "See a doctor immediately — scabies requires prescription medication",
                "All household members and close contacts must be treated at the same time",
                "Wash all clothing, bedding, and towels in hot water (60°C+) and dry on high heat",
                "Items that cannot be washed: seal in plastic bags for 3 days",
                "Vacuum furniture and floors thoroughly",
                "Itching may continue for 2-4 weeks after successful treatment — this is normal"
            ),
            thingsToAvoid = listOf(
                "Close physical contact until treatment is complete",
                "Sharing bedding, clothing, or towels",
                "Scratching — it can cause bacterial infections"
            ),
            otcTreatmentCategories = listOf(
                "OTC treatments are NOT reliable — prescription scabicide cream required"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Norwegian/crusted scabies: thick, widespread crusts — highly contagious, seek urgent care",
                "Signs of secondary bacterial infection: increased redness, warmth, pus, fever"
            ),
            doctorType = "General Practitioner or Dermatologist",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("scabies", "mites", "itching night", "burrow", "contagious itch")
        ),

        SkinCondition(
            id = "impetigo",
            name = "Impetigo",
            category = "Infection",
            description = "Impetigo is a common and highly contagious bacterial skin infection that mainly affects infants and young children. It appears as red sores that quickly rupture, ooze for a few days, and then form a honey-colored crust. It can occur anywhere on the body but most often affects the face, hands, and feet.",
            symptoms = listOf(
                "Red sores that quickly break open, ooze, and form honey-colored crusts",
                "Itchy rash",
                "Blisters filled with fluid (bullous impetigo)",
                "Sores usually around the nose and mouth, but can be anywhere",
                "Swollen lymph nodes in some cases"
            ),
            whoIsAffected = "Most common in children ages 2-5. Spreads through direct contact with sores or nasal discharge of an infected person.",
            affectedAreas = listOf("Face (around nose and mouth)", "Hands", "Arms", "Legs", "Diaper area in infants"),
            homeCareSteps = listOf(
                "See a doctor — antibiotic treatment is usually needed",
                "Keep the area clean: gently wash with soap and water",
                "Do not pick at or scratch the sores",
                "Cover the sores loosely with gauze or bandages",
                "Wash hands frequently",
                "Keep infected child home from school until sores heal or 48h after starting antibiotics"
            ),
            thingsToAvoid = listOf(
                "Touching the sores with bare hands",
                "Sharing towels, clothing, or bedding",
                "Scratching — spreads infection"
            ),
            otcTreatmentCategories = listOf(
                "Topical antibiotic ointments (minor cases)",
                "Prescription oral antibiotics for widespread infection"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Fever with spreading rash",
                "Sores spreading rapidly despite treatment",
                "Swollen, red lymph nodes",
                "Painful skin that looks burned — seek emergency care (rare complication)"
            ),
            doctorType = "General Practitioner or Pediatrician",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("impetigo", "honey crust", "bacterial skin infection", "children skin infection", "crusty sores")
        ),

        SkinCondition(
            id = "cellulitis",
            name = "Cellulitis",
            category = "Infection",
            description = "Cellulitis is a common bacterial skin infection that affects the deeper layers of skin. It usually appears as a swollen, red area that feels warm and tender to the touch. It can spread rapidly and requires prompt antibiotic treatment. It is NOT contagious.",
            symptoms = listOf(
                "Red, swollen, warm area of skin — usually on the legs",
                "Tenderness and pain in the affected area",
                "Skin that may feel tight, glossy, or stretched",
                "Fever, chills, and feeling unwell in more severe cases",
                "Red streaks spreading from the area (lymphangitis — serious sign)",
                "Blisters in severe cases"
            ),
            whoIsAffected = "Anyone, but especially people with cuts, wounds, insect bites, or underlying conditions like diabetes, poor circulation, or lymphedema.",
            affectedAreas = listOf("Lower legs (most common)", "Arms", "Face", "Any skin area with a break"),
            homeCareSteps = listOf(
                "See a doctor immediately — cellulitis requires prescription antibiotics",
                "Do NOT wait to see if it improves on its own",
                "Elevate the affected limb to reduce swelling",
                "Keep the area clean",
                "Mark the border of redness with a pen — if it spreads past the mark, go to emergency"
            ),
            thingsToAvoid = listOf(
                "Delaying medical treatment — cellulitis can worsen rapidly",
                "Warm compresses without medical advice"
            ),
            otcTreatmentCategories = listOf(
                "OTC treatments are NOT sufficient — prescription antibiotics required"
            ),
            urgencyLevel = UrgencyLevel.HIGH,
            emergencyWarnings = listOf(
                "Red streaks spreading from the infection (indicates spread to lymph vessels)",
                "High fever (above 38.5°C / 101.3°F) with chills",
                "Rapidly expanding redness",
                "Skin turning purple or black",
                "If on the face — seek emergency care immediately"
            ),
            doctorType = "Emergency Room or General Practitioner (urgent)",
            isContagious = false,
            isDangerous = true,
            searchTags = listOf("cellulitis", "red swollen skin", "bacterial skin infection", "warm skin", "spreading redness")
        ),

        SkinCondition(
            id = "folliculitis",
            name = "Folliculitis",
            category = "Infection",
            description = "Folliculitis is inflammation of the hair follicles, usually caused by a bacterial infection. It appears as clusters of small, red, pimple-like bumps around hair follicles. Mild cases often clear up on their own with good hygiene.",
            symptoms = listOf(
                "Clusters of small red bumps or whiteheads around hair follicles",
                "Pus-filled blisters that break open and crust over",
                "Itchy, tender, or burning skin",
                "Large, painful bumps or boils (in severe cases)"
            ),
            whoIsAffected = "Anyone, but more common in people who shave, wear tight clothing, use hot tubs, or have oily skin or acne.",
            affectedAreas = listOf("Beard area", "Arms", "Back", "Thighs", "Buttocks", "Scalp"),
            homeCareSteps = listOf(
                "Wash affected area with gentle antibacterial soap",
                "Apply warm, moist compresses for 15-20 minutes 3-4 times daily",
                "Avoid shaving the affected area temporarily",
                "Wear loose-fitting breathable clothing",
                "Do not pop or squeeze the bumps — this spreads infection",
                "Change razor blades frequently if you shave"
            ),
            thingsToAvoid = listOf(
                "Shaving inflamed areas",
                "Tight clothing that rubs the affected area",
                "Hot tubs not properly maintained (hot tub folliculitis)",
                "Touching or scratching the bumps"
            ),
            otcTreatmentCategories = listOf(
                "Antibacterial cleansers",
                "Topical antibiotic ointments (for minor cases)"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Large, painful boil or abscess forming",
                "Fever or spreading infection",
                "Folliculitis on face near eyes or nose"
            ),
            doctorType = "General Practitioner or Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("folliculitis", "hair follicle infection", "red bumps hair", "razor bumps", "pimples back")
        ),

        SkinCondition(
            id = "molluscum_contagiosum",
            name = "Molluscum Contagiosum",
            category = "Infection",
            description = "Molluscum contagiosum is a common viral skin infection caused by the poxvirus family. It causes small, round, firm bumps with a dimple in the center. In healthy children and adults it usually clears on its own within 6-12 months without treatment.",
            symptoms = listOf(
                "Small (2-5mm), round, flesh-colored or pearly bumps",
                "Dimple or pit in the center of each bump",
                "Usually not painful, but may be itchy",
                "Bumps appear in clusters or scattered groups",
                "Can spread to other areas from scratching"
            ),
            whoIsAffected = "Most common in children ages 1-10. Also common in sexually active adults (genital area) and people with weakened immune systems.",
            affectedAreas = listOf("Face", "Neck", "Armpits", "Arms", "Hands", "Trunk", "Genital area in adults"),
            homeCareSteps = listOf(
                "Often best to leave them alone — they disappear on their own in 6-18 months",
                "Keep the area clean to prevent secondary infection",
                "Avoid scratching or picking — this spreads the virus",
                "Cover bumps during swimming or contact sports",
                "Do not share towels, clothing, or personal items"
            ),
            thingsToAvoid = listOf(
                "Scratching or picking at bumps",
                "Sharing towels or clothing",
                "Skin-to-skin contact in sports until resolved"
            ),
            otcTreatmentCategories = listOf(
                "Most cases resolve without treatment",
                "Topical immune-modulating creams available on prescription"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Widespread, rapidly growing lesions in immunocompromised patients — see doctor urgently",
                "Genital area lesions in adults — STI testing advised"
            ),
            doctorType = "Dermatologist or General Practitioner",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("molluscum", "pearly bumps", "dimple bump", "viral skin", "children bumps")
        ),

        SkinCondition(
            id = "warts_verruca",
            name = "Warts (Verruca)",
            category = "Infection",
            description = "Warts are small, rough skin growths caused by the human papillomavirus (HPV). They are very common and usually harmless. Plantar warts appear on the soles of the feet and can be painful. Warts often clear on their own but may take months to years.",
            symptoms = listOf(
                "Small, rough, grainy bumps on the skin",
                "Flesh-colored, white, pink, or tan appearance",
                "Tiny black dots (clotted blood vessels) may be visible",
                "Plantar warts: flat, hard growths on soles — often painful when walking",
                "Mosaic warts: clusters of plantar warts"
            ),
            whoIsAffected = "Very common, especially in children and young adults. Spreads through direct contact with HPV, often in public pools, showers, or by touching warts.",
            affectedAreas = listOf("Hands", "Fingers", "Feet (plantar)", "Face", "Genitals (different strain)"),
            homeCareSteps = listOf(
                "Salicylic acid treatments (pharmacy) applied daily for weeks",
                "Soak wart in warm water for 5 minutes before applying treatment",
                "File with pumice stone or emery board after soaking",
                "Cover with a plaster between treatments",
                "Be patient — treatment takes 3-4 months",
                "Do not pick or scratch warts"
            ),
            thingsToAvoid = listOf(
                "Picking or biting warts",
                "Walking barefoot in public showers or pools",
                "Sharing personal items (razors, towels)"
            ),
            otcTreatmentCategories = listOf(
                "Salicylic acid solutions, gels, or patches",
                "Cryotherapy freeze sprays (home versions available)"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Wart on face, genitals, or rapidly multiplying — see a doctor",
                "Painful plantar wart affecting walking — doctor can perform professional cryotherapy"
            ),
            doctorType = "General Practitioner or Dermatologist",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("warts", "verruca", "HPV skin", "rough bumps", "plantar wart", "foot wart")
        ),

        SkinCondition(
            id = "herpes_simplex",
            name = "Cold Sores (Herpes Simplex)",
            category = "Infection",
            description = "Cold sores are small blisters that develop on the lips or around the mouth. They are caused by the herpes simplex virus type 1 (HSV-1). Once infected, the virus stays in the body permanently and can reactivate, causing recurrent sores. They are very common and usually mild.",
            symptoms = listOf(
                "Tingling, itching, or burning sensation before blisters appear",
                "Small, fluid-filled blisters on or around lips",
                "Blisters that burst and form a painful ulcer",
                "Crust or scab that forms as it heals",
                "Full healing in 2-4 weeks"
            ),
            whoIsAffected = "Extremely common — over 50% of adults carry the virus. Spreads through kissing, sharing utensils, or touching an active sore.",
            affectedAreas = listOf("Lips (most common)", "Around mouth", "Inside mouth (primary infection)", "Chin", "Nose"),
            homeCareSteps = listOf(
                "Apply antiviral cream at first tingle — most effective when used early",
                "Keep the area clean and dry",
                "Avoid touching the sore and then touching eyes or genitals",
                "Use lip balm with sunscreen to prevent recurrence from sun exposure",
                "Get enough sleep and manage stress — triggers recurrence",
                "Cold, damp cloth on sore can relieve pain"
            ),
            thingsToAvoid = listOf(
                "Kissing or sharing cups, cutlery, or lip products during outbreak",
                "Touching the sore then touching eyes (can cause serious eye infection)",
                "Squeezing or picking at blisters",
                "Sunburn on lips (major trigger)"
            ),
            otcTreatmentCategories = listOf(
                "Topical antiviral creams (aciclovir based)",
                "Lidocaine-based gels for pain relief"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Eye redness/pain after touching a cold sore — see doctor immediately (herpetic eye infection)",
                "Cold sore in newborn — medical emergency",
                "Severe outbreak with high fever or confusion",
                "Immunocompromised patients: see doctor at first sign of outbreak"
            ),
            doctorType = "General Practitioner",
            isContagious = true,
            isDangerous = false,
            searchTags = listOf("cold sore", "herpes simplex", "lip blister", "fever blister", "HSV")
        ),

        SkinCondition(
            id = "candidiasis",
            name = "Candidiasis (Yeast/Thrush)",
            category = "Infection",
            description = "Candidiasis is a fungal infection caused by Candida yeast — a normally harmless organism that lives on skin and in the body. When conditions allow it to overgrow (warmth, moisture, antibiotic use), it causes an itchy, red rash. Very common in skin folds, diaper areas, and the mouth.",
            symptoms = listOf(
                "Red, itchy, raw-looking rash in skin folds",
                "Satellite lesions: small red bumps or pustules outside the main rash area",
                "White or yellow discharge (vaginal/oral thrush)",
                "White coating on tongue or inner cheeks (oral thrush)",
                "Burning or soreness in the mouth or affected area",
                "Diaper rash that doesn't improve with standard diaper cream"
            ),
            whoIsAffected = "Infants (diaper area and oral thrush), people taking antibiotics or steroids, diabetics, immunocompromised individuals, and people with skin folds (under breasts, groin, armpits).",
            affectedAreas = listOf("Skin folds", "Groin", "Underarms", "Under breasts", "Diaper area in infants", "Mouth (oral thrush)", "Genitals"),
            homeCareSteps = listOf(
                "Keep affected areas clean and dry — moisture encourages yeast growth",
                "Apply antifungal cream or powder to skin areas",
                "Change diapers frequently for infants",
                "Wear loose, breathable clothing",
                "For oral thrush: rinse mouth with antifungal solution as prescribed",
                "Eat plain yogurt with live cultures (may help balance natural flora)"
            ),
            thingsToAvoid = listOf(
                "Tight, synthetic clothing that traps moisture",
                "Long-term use of antibiotics without medical reason",
                "Leaving wet clothing or swimwear on for extended periods"
            ),
            otcTreatmentCategories = listOf(
                "Antifungal creams (clotrimazole or miconazole based) for skin",
                "Antifungal vaginal suppositories (for vaginal thrush)"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Candidiasis spreading extensively in immunocompromised patients — urgent medical care",
                "Difficulty swallowing with thrush — esophageal involvement, see doctor urgently"
            ),
            doctorType = "General Practitioner or Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("yeast infection", "thrush", "candida", "fungal rash", "diaper rash", "oral thrush")
        ),

        // ─── INFLAMMATORY ────────────────────────────────────────────────────────

        SkinCondition(
            id = "atopic_dermatitis",
            name = "Eczema (Atopic Dermatitis)",
            category = "Inflammatory",
            description = "Eczema is a common condition that makes skin red, inflamed, and intensely itchy. It is not contagious. It tends to run in families and is linked to asthma and hay fever (called the atopic triad). There is no cure, but it can be managed well with proper skin care and treatment.",
            symptoms = listOf(
                "Dry, sensitive skin",
                "Intense itching — often worse at night",
                "Red to brownish-gray patches",
                "Small, raised bumps that may weep fluid when scratched",
                "Thickened, cracked, or scaly skin",
                "Raw, swollen skin from scratching",
                "Common in babies: face and scalp; in adults: hands, neck, inside elbows/knees"
            ),
            whoIsAffected = "Affects about 10-20% of children and 1-3% of adults worldwide. Often starts in infancy. Children may grow out of it, but many carry it into adulthood.",
            affectedAreas = listOf("Elbows (inner)", "Knees (behind)", "Neck", "Face", "Wrists", "Ankles", "Hands"),
            homeCareSteps = listOf(
                "Moisturize skin at least twice daily — immediately after bathing",
                "Use gentle, fragrance-free moisturizers (creams better than lotions)",
                "Take short, lukewarm baths — hot water worsens dryness",
                "Use mild, fragrance-free soap",
                "Identify and avoid triggers: certain soaps, detergents, stress, sweat, heat",
                "Wear soft, breathable cotton clothing",
                "Keep fingernails short to reduce damage from scratching",
                "Use prescribed topical steroid cream during flare-ups"
            ),
            thingsToAvoid = listOf(
                "Harsh soaps, detergents, or bubble baths",
                "Scratchy fabrics like wool",
                "Rapid temperature changes",
                "Known triggers: dust mites, pet dander, pollen, certain foods in some cases",
                "Hot, dry environments"
            ),
            otcTreatmentCategories = listOf(
                "Fragrance-free emollient creams (applied liberally and frequently)",
                "Hydrocortisone cream (mild steroid) for short-term flare control",
                "Antihistamines for nighttime itching relief"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Signs of skin infection (yellow crusting, pus, fever) — eczema skin is vulnerable to bacterial infection",
                "Herpetic eczema (sudden painful blisters spreading quickly) — medical emergency",
                "Widespread severe flare unresponsive to home care"
            ),
            doctorType = "Dermatologist or Allergist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("eczema", "atopic dermatitis", "itchy skin", "dry skin", "rash", "skin inflammation")
        ),

        SkinCondition(
            id = "psoriasis",
            name = "Psoriasis",
            category = "Inflammatory",
            description = "Psoriasis is a chronic autoimmune condition that speeds up the life cycle of skin cells. Cells build up rapidly on the skin surface, forming scales and red patches that can be itchy and sometimes painful. It tends to go through cycles — flaring for a few weeks or months, then easing or going into remission.",
            symptoms = listOf(
                "Red patches covered with thick, silvery scales",
                "Dry, cracked skin that may bleed",
                "Itching, burning, or soreness",
                "Thickened, pitted, or ridged nails",
                "Swollen and stiff joints (psoriatic arthritis in some cases)",
                "Common sites: elbows, knees, scalp, lower back"
            ),
            whoIsAffected = "Affects about 2-3% of the population worldwide. Can start at any age — two peaks: 20s and 50-60s. Not contagious.",
            affectedAreas = listOf("Elbows", "Knees", "Scalp", "Lower back", "Face", "Palms", "Soles of feet"),
            homeCareSteps = listOf(
                "Keep skin well moisturized — apply thick cream after bathing",
                "Take short, warm (not hot) baths with colloidal oatmeal or bath oil",
                "Expose skin to small amounts of sunlight daily (avoid sunburn)",
                "Manage stress — a major trigger for flares",
                "Avoid smoking and excessive alcohol (worsen psoriasis)",
                "Identify personal triggers: stress, infections, certain medications",
                "Use prescribed treatments consistently as directed by doctor"
            ),
            thingsToAvoid = listOf(
                "Scratching or picking at scales — causes bleeding and Koebner phenomenon",
                "Skin injuries (cuts, scrapes) — can trigger new plaques",
                "Stress",
                "Smoking",
                "Alcohol",
                "Certain medications: beta-blockers, lithium, some antimalarials"
            ),
            otcTreatmentCategories = listOf(
                "Coal tar shampoos and creams",
                "Salicylic acid products for scale removal",
                "Fragrance-free moisturizers"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Erythrodermic psoriasis: skin almost entirely red and shedding — medical emergency",
                "Pustular psoriasis: widespread pus-filled blisters — seek urgent care",
                "Psoriatic arthritis: joint pain and swelling — see rheumatologist"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("psoriasis", "silver scales", "red plaques", "autoimmune skin", "scaly skin", "chronic skin")
        ),

        SkinCondition(
            id = "seborrheic_dermatitis",
            name = "Seborrheic Dermatitis",
            category = "Inflammatory",
            description = "Seborrheic dermatitis is a common skin condition that mainly affects the scalp, causing scaly patches, red skin, and stubborn dandruff. It can also affect oily areas of the body — sides of the nose, eyebrows, ears, and chest. It is a chronic condition that tends to recur.",
            symptoms = listOf(
                "Flaking skin (dandruff) on scalp, hair, eyebrows, beard, or mustache",
                "Patches of greasy or oily skin covered with white or yellowish scales",
                "Red skin",
                "Itching",
                "In infants: cradle cap — thick, crusty, yellowish patches on scalp"
            ),
            whoIsAffected = "Very common — affects up to 5% of the general population. More common in men, people with neurological conditions (Parkinson's), or HIV.",
            affectedAreas = listOf("Scalp", "Face (T-zone)", "Behind ears", "Inside ears", "Eyebrows", "Nose sides", "Chest"),
            homeCareSteps = listOf(
                "Use medicated dandruff shampoo regularly (2-3 times per week initially)",
                "Let shampoo sit for 5 minutes before rinsing",
                "Wash face daily with gentle cleanser",
                "Gently remove scales with soft brush or comb",
                "Manage stress — can trigger flares",
                "Get sunlight exposure (natural anti-inflammatory effect)",
                "For infants: apply baby oil to soften cradle cap, then gently comb off"
            ),
            thingsToAvoid = listOf(
                "Harsh, fragranced hair or skin products",
                "Scratching or picking at scales",
                "Stress",
                "Cold, dry weather (worsens condition)"
            ),
            otcTreatmentCategories = listOf(
                "Medicated shampoos containing: zinc pyrithione, selenium sulfide, coal tar, or ketoconazole",
                "Antifungal creams for facial or body seborrheic dermatitis"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Widespread, severe rash with systemic symptoms — may indicate underlying immunodeficiency"
            ),
            doctorType = "Dermatologist or General Practitioner",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("dandruff", "seborrheic dermatitis", "scalp flakes", "oily skin rash", "cradle cap", "flaky scalp")
        ),

        SkinCondition(
            id = "contact_dermatitis",
            name = "Contact Dermatitis",
            category = "Inflammatory",
            description = "Contact dermatitis is a red, itchy rash caused by direct contact with a substance that irritates your skin or causes an allergic reaction. The rash appears where the skin touched the irritant or allergen. Common causes include soap, cosmetics, fragrances, jewelry (nickel), and plants like poison ivy.",
            symptoms = listOf(
                "Red rash in the area that touched the irritant",
                "Intense itching",
                "Dry, cracked, scaly skin (irritant contact dermatitis)",
                "Blisters or bumps that ooze (allergic contact dermatitis)",
                "Burning or stinging sensation",
                "Rash precisely matches the area of contact"
            ),
            whoIsAffected = "Anyone can develop contact dermatitis. Healthcare workers, hairdressers, food handlers, and metalworkers have higher exposure to irritants.",
            affectedAreas = listOf("Hands (most common)", "Face", "Neck", "Any area that contacted the substance"),
            homeCareSteps = listOf(
                "Identify and avoid the irritant or allergen — the most important step",
                "Rinse skin with cool water for 15-20 minutes if still in contact with irritant",
                "Apply cool, wet compresses to relieve itching and inflammation",
                "Use fragrance-free moisturizer to soothe and protect skin",
                "Apply mild hydrocortisone cream for itching",
                "Take antihistamine tablets for relief"
            ),
            thingsToAvoid = listOf(
                "The identified irritant or allergen",
                "Scratching — can cause infection",
                "Fragranced soaps, lotions, or cosmetics",
                "Jewelry containing nickel if nickel-allergic"
            ),
            otcTreatmentCategories = listOf(
                "Hydrocortisone cream (1%) for inflammation",
                "Oral antihistamines for itching",
                "Calamine lotion for soothing"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Rash on face with eye involvement — seek urgent care",
                "Rash spreads over large area of body",
                "Difficulty breathing or swallowing after exposure — anaphylaxis emergency, call ambulance"
            ),
            doctorType = "Dermatologist or Allergist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("contact dermatitis", "allergic rash", "irritant rash", "nickel allergy", "poison ivy", "soap rash")
        ),

        SkinCondition(
            id = "rosacea",
            name = "Rosacea",
            category = "Inflammatory",
            description = "Rosacea is a common, chronic skin condition that causes redness and visible blood vessels on the face. It may also produce small, red, pus-filled bumps that look like acne. It tends to flare up in cycles — triggered by sun exposure, heat, spicy food, alcohol, or stress.",
            symptoms = listOf(
                "Facial redness (flushing and persistent redness — usually central face)",
                "Visible small blood vessels (spider veins) on nose and cheeks",
                "Red, swollen bumps that look like acne — may contain pus",
                "Eye problems: dry, irritated, swollen, reddened eyes (ocular rosacea)",
                "Enlarged nose: skin on nose thickens and enlarges (rhinophyma — mostly in men)",
                "Burning or stinging sensation on face"
            ),
            whoIsAffected = "More common in fair-skinned women ages 30-50, but can affect anyone. Family history increases risk.",
            affectedAreas = listOf("Nose", "Cheeks", "Forehead", "Chin", "Eyes (ocular rosacea)"),
            homeCareSteps = listOf(
                "Identify and avoid your personal triggers (sunlight, heat, spicy food, alcohol)",
                "Apply broad-spectrum SPF 30+ sunscreen daily",
                "Use gentle, fragrance-free skincare products",
                "Wash face with lukewarm water and pat dry gently",
                "Avoid very hot foods and beverages",
                "Protect face from wind and cold",
                "Manage stress"
            ),
            thingsToAvoid = listOf(
                "Sun exposure without sunscreen",
                "Extreme temperatures (very hot or cold)",
                "Spicy foods and hot beverages",
                "Alcohol",
                "Harsh scrubs or exfoliants on face",
                "Certain skincare ingredients: alcohol, fragrance, menthol"
            ),
            otcTreatmentCategories = listOf(
                "Gentle, fragrance-free moisturizers",
                "Mineral sunscreen (physical SPF)",
                "Green-tinted makeup for color correction"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Ocular rosacea with vision changes — see ophthalmologist urgently",
                "Rapidly worsening skin symptoms"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("rosacea", "facial redness", "red face", "acne rosacea", "flushing face", "broken veins face")
        ),

        SkinCondition(
            id = "acne_vulgaris",
            name = "Acne",
            category = "Inflammatory",
            description = "Acne is one of the most common skin conditions in the world. It occurs when hair follicles become clogged with oil and dead skin cells. It can cause whiteheads, blackheads, and pimples. While mainly affecting teenagers, acne can affect people of all ages.",
            symptoms = listOf(
                "Whiteheads (closed clogged pores)",
                "Blackheads (open clogged pores — dark because of air oxidation, NOT dirt)",
                "Small red, tender bumps (papules)",
                "Pimples: papules with pus at tips (pustules)",
                "Large, solid, painful lumps under skin (nodules)",
                "Painful, pus-filled lumps (cystic acne — can cause scarring)"
            ),
            whoIsAffected = "About 85% of people between ages 12-24 experience acne. Hormonal changes, genetics, and certain medications can cause adult acne.",
            affectedAreas = listOf("Face", "Forehead", "Chest", "Upper back", "Shoulders"),
            homeCareSteps = listOf(
                "Wash face twice daily with gentle, non-comedogenic cleanser",
                "Do not scrub face harshly — this irritates and worsens acne",
                "Use oil-free, non-comedogenic moisturizer and makeup",
                "Do not pop, squeeze, or pick pimples — causes scarring and spreads bacteria",
                "Change pillowcases weekly",
                "Shower after exercising to remove sweat",
                "Manage stress — triggers hormonal acne"
            ),
            thingsToAvoid = listOf(
                "Touching face frequently",
                "Squeezing or popping pimples",
                "Heavy, oily skincare or makeup",
                "Dairy and high-glycemic foods (may worsen acne in some people)",
                "Tight clothing over chest/back acne"
            ),
            otcTreatmentCategories = listOf(
                "Benzoyl peroxide washes and gels (kills acne bacteria)",
                "Salicylic acid cleansers and toners (unclogs pores)",
                "Adapalene gel (retinoid — increases cell turnover)"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Severe cystic acne covering large areas — see dermatologist to prevent permanent scarring",
                "Acne with fever and swollen lymph nodes — rare, see doctor"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("acne", "pimples", "blackheads", "whiteheads", "cystic acne", "breakouts", "zits")
        ),

        SkinCondition(
            id = "lichen_planus",
            name = "Lichen Planus",
            category = "Inflammatory",
            description = "Lichen planus is an inflammatory condition affecting the skin, hair, nails, and mucous membranes. On the skin, it appears as flat-topped, purple-ish, itchy bumps. In the mouth, it appears as white, lacy patches. The cause is unknown but is thought to be an immune response.",
            symptoms = listOf(
                "Flat-topped, purple or violet-colored bumps on skin",
                "Bumps with white lines or scales on top (Wickham's striae)",
                "Intense itching",
                "White, lacy patches inside the mouth (can be painful or sore)",
                "Nail changes: ridging, thinning, splitting",
                "Patches often appear on wrists, lower legs, and back"
            ),
            whoIsAffected = "Can affect anyone, but most common in middle-aged adults. May be triggered by hepatitis C infection, certain medications, or dental materials.",
            affectedAreas = listOf("Wrists", "Lower legs", "Lower back", "Inner mouth", "Genitals", "Nails", "Scalp"),
            homeCareSteps = listOf(
                "See a doctor for diagnosis and treatment plan",
                "Apply prescribed topical steroid cream to skin lesions",
                "Take antihistamine at night to control itching",
                "Avoid scratching to prevent spreading",
                "Use soft toothbrush for oral lichen planus",
                "Avoid spicy food and alcohol if oral lesions are present"
            ),
            thingsToAvoid = listOf(
                "Scratching or rubbing lesions",
                "Known trigger medications (discuss with doctor)",
                "Spicy foods with oral lesions"
            ),
            otcTreatmentCategories = listOf(
                "Antihistamines for itching (short-term relief only)",
                "Prescription corticosteroids are the main treatment"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Painful oral lesions that prevent eating or drinking — urgent dental/medical review",
                "Oral lichen planus requires monitoring as it has a small risk of oral cancer long-term"
            ),
            doctorType = "Dermatologist or oral medicine specialist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("lichen planus", "purple bumps", "flat bumps", "itchy wrist", "white mouth patches", "nail damage")
        ),

        SkinCondition(
            id = "pityriasis_rosea",
            name = "Pityriasis Rosea",
            category = "Inflammatory",
            description = "Pityriasis rosea is a common, harmless skin rash that usually starts with one large oval patch (herald patch) and then spreads to form a Christmas-tree pattern on the trunk. It usually clears by itself within 6-12 weeks. It is thought to be caused by a viral infection.",
            symptoms = listOf(
                "Herald patch: one large (2-10cm) oval, pink, scaly patch appearing first",
                "After 1-2 weeks: smaller oval patches spread across trunk, back, arms",
                "Rash in a 'Christmas tree' pattern following lines of ribs on back",
                "Mild to moderate itching",
                "Headache, fatigue, fever may precede the rash",
                "Rash rarely affects face or extremities"
            ),
            whoIsAffected = "Most common in people ages 10-35. Slightly more common in women. Occurs in spring and autumn.",
            affectedAreas = listOf("Trunk (chest and back)", "Abdomen", "Neck", "Upper arms", "Upper thighs"),
            homeCareSteps = listOf(
                "Condition resolves on its own in 6-12 weeks — reassurance is key",
                "Antihistamines can help with itching",
                "Apply mild moisturizers to soothe skin",
                "Lukewarm (not hot) baths — heat worsens itching",
                "Sunlight exposure may speed healing for some people",
                "Avoid scratching"
            ),
            thingsToAvoid = listOf(
                "Hot showers (worsens itching)",
                "Scratching",
                "Tight clothing that rubs affected area"
            ),
            otcTreatmentCategories = listOf(
                "Antihistamines for itching relief",
                "Calamine lotion",
                "Gentle moisturizers"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Rash persists beyond 3 months without improvement — see doctor for reassessment",
                "Pregnant women should see a doctor — rare association with complications"
            ),
            doctorType = "General Practitioner or Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("pityriasis rosea", "herald patch", "christmas tree rash", "oval rash back", "self-limiting rash")
        ),

        SkinCondition(
            id = "urticaria",
            name = "Hives (Urticaria)",
            category = "Inflammatory",
            description = "Hives are raised, itchy, red or skin-colored welts that appear suddenly on any part of the body. They often appear as a reaction to food, medication, insect stings, or infections. Individual welts come and go within 24 hours, but new ones may keep appearing. Most cases resolve within 6 weeks.",
            symptoms = listOf(
                "Raised, red or skin-colored welts of varying sizes",
                "Welts that may merge to form larger areas",
                "Intense itching",
                "Welts appear and disappear within 24 hours",
                "Burning or stinging sensation",
                "Angioedema: deeper swelling of lips, eyes, hands, or throat (serious)"
            ),
            whoIsAffected = "Up to 20% of people experience hives at some point in their life. Can affect anyone at any age.",
            affectedAreas = listOf("Any body surface", "Face", "Lips and throat (angioedema — serious)"),
            homeCareSteps = listOf(
                "Take antihistamine tablets as soon as hives appear",
                "Apply cool, damp cloths to affected areas for relief",
                "Wear loose, smooth-textured cotton clothing",
                "Avoid known triggers: certain foods, medications, stress, heat, exercise",
                "Take cool (not hot) showers",
                "Keep antihistamines on hand for future outbreaks"
            ),
            thingsToAvoid = listOf(
                "Known triggers (foods, medications, stress, heat)",
                "Scratching — worsens and spreads hives",
                "Hot showers or baths",
                "Tight clothing"
            ),
            otcTreatmentCategories = listOf(
                "Non-drowsy antihistamines (cetirizine, loratadine, or fexofenadine based)",
                "Calamine lotion for topical soothing"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Swelling of lips, tongue, or throat — call emergency services IMMEDIATELY (anaphylaxis)",
                "Difficulty breathing or swallowing — emergency",
                "Dizziness, low blood pressure — emergency",
                "Hives lasting more than 6 weeks — chronic urticaria, see doctor"
            ),
            doctorType = "Emergency Room (if throat swelling), Allergist or General Practitioner",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("hives", "urticaria", "welts", "allergic rash", "swollen skin", "itchy bumps")
        ),

        SkinCondition(
            id = "perioral_dermatitis",
            name = "Perioral Dermatitis",
            category = "Inflammatory",
            description = "Perioral dermatitis is a red, bumpy rash that appears around the mouth. Despite looking like acne, it is a different condition. It is commonly triggered by the overuse of topical steroid creams on the face. It responds well to treatment but may take weeks to months to fully clear.",
            symptoms = listOf(
                "Small red or pink bumps and pustules around the mouth",
                "Rash typically leaves a pale ring of normal skin directly around the lips",
                "May spread to around the nose or eyes",
                "Mild burning, itching, or tightness",
                "Rash may worsen when steroid cream is stopped (rebound effect)"
            ),
            whoIsAffected = "Most common in young women ages 15-45. Can also occur in children. Associated with use of topical steroids or fluorinated toothpaste.",
            affectedAreas = listOf("Around mouth", "Around nose", "Around eyes (less common)"),
            homeCareSteps = listOf(
                "See a doctor — prescription antibiotics are usually needed",
                "Stop using any steroid cream on the face (under doctor's guidance — expect brief worsening)",
                "Switch to SLS-free (sodium lauryl sulfate free) toothpaste",
                "Use only gentle, minimal skincare products",
                "Avoid heavy moisturizers or creams on the face"
            ),
            thingsToAvoid = listOf(
                "Topical steroid creams on the face",
                "Fluorinated toothpaste",
                "Heavy face creams or foundations",
                "Picking or squeezing bumps"
            ),
            otcTreatmentCategories = listOf(
                "Prescription oral antibiotics are the standard treatment",
                "Prescription topical antibiotics or azelaic acid cream"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "No specific emergencies — but see a doctor before starting any treatment"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("perioral dermatitis", "mouth rash", "bumps around mouth", "steroid rash face", "rash around lips")
        ),

        // ─── PIGMENTATION ────────────────────────────────────────────────────────

        SkinCondition(
            id = "vitiligo",
            name = "Vitiligo",
            category = "Pigmentation",
            description = "Vitiligo is a condition in which the immune system attacks the pigment-producing cells (melanocytes) in the skin, causing white patches to appear. It can affect any part of the body and sometimes the hair. Vitiligo is not contagious and not life-threatening, but it can affect self-esteem.",
            symptoms = listOf(
                "Milky-white patches of skin (depigmented areas)",
                "Premature whitening of hair, eyelashes, or eyebrows",
                "Loss of color inside mouth or nose",
                "Patches may appear on any part of the body",
                "Patches may gradually enlarge over time",
                "Skin is otherwise normal — no itching or pain"
            ),
            whoIsAffected = "Affects about 1-2% of people worldwide, across all skin tones. Often starts before age 20. More noticeable on darker skin tones.",
            affectedAreas = listOf("Face", "Hands", "Feet", "Arms", "Lips", "Genitals", "Any body area"),
            homeCareSteps = listOf(
                "Apply SPF 30+ sunscreen to depigmented patches — they sunburn very easily",
                "Camouflage makeup or self-tanning products can help aesthetically",
                "See a dermatologist to discuss medical treatment options",
                "Manage stress — possible trigger for spread",
                "Seek support groups — vitiligo can affect mental health"
            ),
            thingsToAvoid = listOf(
                "Sunburn on depigmented areas",
                "Skin trauma — can cause new patches (Koebner phenomenon)",
                "Tattooing over affected areas without medical advice"
            ),
            otcTreatmentCategories = listOf(
                "High SPF mineral sunscreen (essential)",
                "Self-tanning creams or camouflage products (cosmetic)"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Not medically dangerous, but see a doctor to confirm diagnosis and discuss treatment",
                "Associated with thyroid disease — have thyroid function checked"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("vitiligo", "white patches skin", "depigmentation", "loss of skin color", "white skin patches")
        ),

        SkinCondition(
            id = "melasma",
            name = "Melasma",
            category = "Pigmentation",
            description = "Melasma causes brown or gray-brown patches on the face. It occurs when melanocytes (skin pigment cells) produce too much pigment. Sun exposure and hormonal changes (pregnancy, birth control) are major triggers. It is more common in women and in people with darker skin tones.",
            symptoms = listOf(
                "Brown or gray-brown patches — most often on cheeks, forehead, nose, lips, chin",
                "Patches are flat and have irregular borders",
                "No itching, pain, or other physical symptoms",
                "Patches may darken in summer and lighten in winter"
            ),
            whoIsAffected = "Much more common in women (90% of cases). Highest risk during pregnancy ('mask of pregnancy') or while taking hormonal birth control. More common in darker skin tones and people who have significant sun exposure.",
            affectedAreas = listOf("Cheeks", "Forehead", "Bridge of nose", "Upper lip", "Chin"),
            homeCareSteps = listOf(
                "Daily sun protection is the most important step — use SPF 30+ every day",
                "Wear a wide-brimmed hat outdoors",
                "Use gentle skincare — avoid anything that irritates skin (worsens pigmentation)",
                "Vitamin C serum may help lighten patches over time",
                "See a dermatologist for prescription lightening treatments"
            ),
            thingsToAvoid = listOf(
                "Sun exposure without SPF — worsens melasma significantly",
                "Tanning beds",
                "Irritating skincare products (fragrances, acids used incorrectly)",
                "Hormonal triggers if possible (discuss with doctor)"
            ),
            otcTreatmentCategories = listOf(
                "Daily SPF 30+ broad-spectrum sunscreen (essential)",
                "Vitamin C serum for gradual lightening",
                "Gentle, fragrance-free moisturizer"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Any changing or asymmetric dark patch on face — see dermatologist to rule out melanoma"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("melasma", "brown patches face", "mask of pregnancy", "hyperpigmentation face", "dark spots")
        ),

        SkinCondition(
            id = "pityriasis_versicolor",
            name = "Pityriasis Versicolor",
            category = "Pigmentation",
            description = "Pityriasis versicolor (also called tinea versicolor) is a common fungal infection that causes small, discolored patches on the skin. The patches can be lighter or darker than surrounding skin. It is caused by an overgrowth of naturally occurring skin yeast (Malassezia). It is harmless and not contagious.",
            symptoms = listOf(
                "Small, discolored patches — lighter OR darker than surrounding skin",
                "Patches may be white, pink, red, or brown",
                "Fine, powdery scales on the patches",
                "Mild itching (often none)",
                "Patches become more visible with tanning or in summer",
                "Most common on back, chest, neck, and upper arms"
            ),
            whoIsAffected = "Common in hot, humid climates. Affects teens and young adults most often. More common in people who sweat heavily.",
            affectedAreas = listOf("Back", "Chest", "Shoulders", "Upper arms", "Neck"),
            homeCareSteps = listOf(
                "Use antifungal shampoo on affected areas — leave on for 5-10 minutes, then rinse",
                "Apply antifungal cream or lotion to affected areas",
                "Continue treatment for 2-4 weeks even after improvement",
                "Skin color may take weeks to months to fully return to normal after treatment",
                "Recurrence is common — may need periodic maintenance treatment"
            ),
            thingsToAvoid = listOf(
                "Tight, non-breathable clothing",
                "Excessive sweating without immediate showering",
                "Oily sunscreens or body lotions (feed the yeast)"
            ),
            otcTreatmentCategories = listOf(
                "Antifungal shampoos containing ketoconazole or selenium sulfide",
                "Antifungal creams (miconazole or clotrimazole based)"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Not dangerous, but persistent or widespread infection may need prescription antifungal tablets"
            ),
            doctorType = "General Practitioner or Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("tinea versicolor", "pityriasis versicolor", "discolored patches", "light patches skin", "dark patches skin", "yeast skin")
        ),

        SkinCondition(
            id = "post_inflammatory_hyperpigmentation",
            name = "Post-Inflammatory Hyperpigmentation",
            category = "Pigmentation",
            description = "Post-inflammatory hyperpigmentation (PIH) is darkening of the skin that remains after a skin injury or inflammation has healed — such as after acne, eczema, cuts, or burns. The skin overproduces melanin in response to injury. It is not a disease itself — it is the skin's healing response.",
            symptoms = listOf(
                "Dark brown or grey spots where a skin injury or blemish was",
                "Common after acne spots, eczema patches, insect bites, or minor injuries",
                "No itching, pain, or raised skin",
                "More noticeable and slower to fade in darker skin tones",
                "Gradually fades over months to years without treatment"
            ),
            whoIsAffected = "Can affect anyone, but more common and more pronounced in people with darker skin tones (Fitzpatrick types IV-VI).",
            affectedAreas = listOf("Face (most common)", "Any area where skin was previously inflamed or injured"),
            homeCareSteps = listOf(
                "Protect from sun — UV exposure significantly darkens PIH marks",
                "Apply SPF 30+ daily (essential)",
                "Vitamin C serum applied in the morning can help lighten over time",
                "Niacinamide (vitamin B3) products can gradually reduce pigmentation",
                "Be patient — PIH on darker skin can take 6-24 months to fully fade",
                "Treat the underlying cause (e.g., acne) to prevent new marks"
            ),
            thingsToAvoid = listOf(
                "Sun exposure without SPF",
                "Picking or squeezing acne or blemishes (directly causes PIH)",
                "Harsh exfoliants that irritate skin",
                "Lemon juice or other DIY remedies — can cause further irritation"
            ),
            otcTreatmentCategories = listOf(
                "SPF 30+ broad-spectrum sunscreen (essential)",
                "Vitamin C serums",
                "Niacinamide serums or moisturizers",
                "Alpha arbutin serum"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Changing or asymmetric pigmented spots — see dermatologist to rule out melanoma"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("PIH", "dark spots acne", "post inflammatory", "hyperpigmentation", "acne marks", "dark marks skin")
        ),

        // ─── DANGEROUS CONDITIONS (AWARENESS) ───────────────────────────────────

        SkinCondition(
            id = "melanoma",
            name = "Melanoma (Warning Signs)",
            category = "Dangerous",
            description = "Melanoma is the most dangerous form of skin cancer. It develops from the cells that give skin its color (melanocytes). Early detection is CRITICAL — melanoma found early is highly treatable. Learn the ABCDE warning signs and check your skin monthly. This information is for AWARENESS ONLY — only a dermatologist can diagnose melanoma.",
            symptoms = listOf(
                "A — Asymmetry: one half doesn't match the other",
                "B — Border: irregular, ragged, notched, or blurred edges",
                "C — Color: variation in color (shades of brown, black, red, white, or blue)",
                "D — Diameter: larger than 6mm (size of a pencil eraser) — though can be smaller",
                "E — Evolving: any change in size, shape, color, or a new symptom like bleeding",
                "Sore that doesn't heal",
                "Spread of pigment from the border into surrounding skin",
                "Redness or swelling beyond the mole border"
            ),
            whoIsAffected = "Higher risk: fair skin, many moles, history of sunburns, family history of melanoma, personal history of skin cancer, weakened immune system. Can occur on any skin tone.",
            affectedAreas = listOf("Any body surface", "Back and legs most common", "Under nails", "Palms and soles in darker skin tones"),
            homeCareSteps = listOf(
                "Perform monthly skin self-checks using a full-length mirror",
                "Apply SPF 30+ sunscreen daily — year-round",
                "Avoid tanning beds completely",
                "Know your moles — photograph them to track changes over time",
                "See a dermatologist annually for professional skin check",
                "Do not attempt to treat moles or dark spots at home"
            ),
            thingsToAvoid = listOf(
                "Tanning beds — highest melanoma risk factor",
                "Sunburn — even one severe sunburn increases risk",
                "Delaying a doctor visit if you notice ABCDEs in a mole"
            ),
            otcTreatmentCategories = listOf(
                "NO OTC treatment — see a doctor immediately",
                "Prevention only: broad-spectrum SPF 50 sunscreen"
            ),
            urgencyLevel = UrgencyLevel.EMERGENCY,
            emergencyWarnings = listOf(
                "ANY mole with ABCDE features — see a dermatologist as soon as possible",
                "Rapidly growing new dark spot",
                "Mole that bleeds spontaneously",
                "Melanoma is life-threatening if untreated — early action saves lives"
            ),
            doctorType = "Dermatologist (urgent) — biopsy required for diagnosis",
            isContagious = false,
            isDangerous = true,
            searchTags = listOf("melanoma", "skin cancer", "mole changes", "ABCDE rule", "dangerous mole", "skin cancer warning")
        ),

        SkinCondition(
            id = "basal_cell_carcinoma",
            name = "Basal Cell Carcinoma (Warning Signs)",
            category = "Dangerous",
            description = "Basal cell carcinoma (BCC) is the most common form of skin cancer. It grows slowly and rarely spreads to other parts of the body, but it can cause significant local tissue destruction if left untreated. Most BCCs are caused by long-term sun exposure. It is HIGHLY treatable when caught early. This information is for AWARENESS ONLY.",
            symptoms = listOf(
                "Pearly or waxy bump, often with visible blood vessels",
                "Flat, flesh-colored or brown scar-like lesion",
                "Pink growth with raised edges and a crusted center",
                "Sore that repeatedly heals and then reopens",
                "Pink skin growth with rolled border and depressed center",
                "White, waxy scar in an area of previous injury (morpheaform BCC)"
            ),
            whoIsAffected = "Most common in people over 50, fair-skinned individuals, and those with significant lifetime sun exposure. Very rare on sun-protected skin.",
            affectedAreas = listOf("Face (nose, ears, forehead)", "Neck", "Hands", "Any sun-exposed area"),
            homeCareSteps = listOf(
                "See a dermatologist immediately if you notice any suspicious lesion",
                "Apply SPF 30+ sunscreen daily to all sun-exposed areas",
                "Wear protective clothing, hat, and sunglasses outdoors",
                "Perform monthly skin self-checks",
                "Annual professional skin examination"
            ),
            thingsToAvoid = listOf(
                "Sun exposure without protection",
                "Tanning beds",
                "Ignoring non-healing sores or unusual skin changes"
            ),
            otcTreatmentCategories = listOf(
                "NO OTC treatment — medical evaluation and treatment required",
                "Prevention: broad-spectrum SPF 30+ sunscreen"
            ),
            urgencyLevel = UrgencyLevel.HIGH,
            emergencyWarnings = listOf(
                "Any sore that doesn't heal within 3 weeks — see a doctor",
                "Any lesion with these features — dermatology appointment urgently required",
                "Not emergency room level, but prompt medical evaluation is essential"
            ),
            doctorType = "Dermatologist — biopsy required for diagnosis",
            isContagious = false,
            isDangerous = true,
            searchTags = listOf("basal cell carcinoma", "BCC", "skin cancer", "pearly bump", "non-healing sore", "sun damage cancer")
        ),

        SkinCondition(
            id = "squamous_cell_carcinoma",
            name = "Squamous Cell Carcinoma (Warning Signs)",
            category = "Dangerous",
            description = "Squamous cell carcinoma (SCC) is the second most common form of skin cancer. It can spread (metastasize) if left untreated, making early detection important. It often develops in areas with long-term sun damage or from a precancerous condition called actinic keratosis. This information is for AWARENESS ONLY — medical evaluation required.",
            symptoms = listOf(
                "Firm, red nodule or growth",
                "Flat lesion with a scaly, crusted surface",
                "New sore or raised area on an old scar",
                "Rough, scaly patch on the lip",
                "Red sore or rough patch inside the mouth",
                "Wart-like growth",
                "Sore that doesn't heal or keeps returning"
            ),
            whoIsAffected = "Most common in people over 50 with significant sun exposure, fair skin, or a history of actinic keratosis. Immunocompromised individuals are at higher risk.",
            affectedAreas = listOf("Face", "Ears", "Lips", "Hands and forearms", "Scalp (in bald individuals)", "Legs"),
            homeCareSteps = listOf(
                "See a dermatologist immediately if you notice any suspicious lesion",
                "Apply SPF 30+ sunscreen daily",
                "Wear protective clothing and hats outdoors",
                "Do not smoke — smoking increases SCC risk on lips and mouth",
                "Treat actinic keratoses (precancerous lesions) as advised by doctor"
            ),
            thingsToAvoid = listOf(
                "Sun exposure without protection",
                "Tanning beds",
                "Smoking",
                "Ignoring non-healing or growing skin lesions"
            ),
            otcTreatmentCategories = listOf(
                "NO OTC treatment — medical evaluation required",
                "Prevention: broad-spectrum SPF 50 sunscreen"
            ),
            urgencyLevel = UrgencyLevel.HIGH,
            emergencyWarnings = listOf(
                "Rapidly growing, hardening, or spreading lesion — urgent dermatology visit",
                "Any lesion growing quickly or in a lymph node area",
                "Sore that bleeds easily and doesn't heal"
            ),
            doctorType = "Dermatologist — biopsy required for diagnosis",
            isContagious = false,
            isDangerous = true,
            searchTags = listOf("squamous cell carcinoma", "SCC", "skin cancer", "non-healing sore", "scaly growth", "skin cancer sun damage")
        ),

        SkinCondition(
            id = "actinic_keratosis",
            name = "Actinic Keratosis (Precancerous)",
            category = "Dangerous",
            description = "Actinic keratosis (AK) is a rough, scaly patch on the skin caused by years of sun exposure. It is considered a precancerous condition because it can progress to squamous cell carcinoma if left untreated. Treatment is straightforward when caught early. See a dermatologist for assessment and treatment.",
            symptoms = listOf(
                "Rough, dry, scaly patch (1-2.5cm) on sun-exposed skin",
                "Flat to slightly raised patch — may feel like sandpaper",
                "Color varies: pink, red, or brown",
                "Itching, burning, or tenderness in the patch",
                "Hard, wart-like surface in some forms",
                "Horn-like growth from the lesion (cutaneous horn)"
            ),
            whoIsAffected = "Common after age 40 in people with fair skin and significant sun exposure history. Very common in sun-intensive regions.",
            affectedAreas = listOf("Face", "Lips", "Ears", "Scalp", "Neck", "Backs of hands and forearms"),
            homeCareSteps = listOf(
                "See a dermatologist — do not ignore these lesions",
                "Do not attempt to remove AKs at home",
                "Apply sunscreen SPF 30+ daily",
                "Wear protective clothing and hats",
                "Monthly self-checks for any changes"
            ),
            thingsToAvoid = listOf(
                "Sun exposure without protection",
                "Tanning beds",
                "Trying to scrape or remove the lesion at home"
            ),
            otcTreatmentCategories = listOf(
                "NO OTC treatment — medical treatment required (cryotherapy, prescription creams)",
                "Prevention: SPF 50 broad-spectrum sunscreen"
            ),
            urgencyLevel = UrgencyLevel.HIGH,
            emergencyWarnings = listOf(
                "AK that becomes painful, bleeds, or hardens rapidly — see dermatologist urgently",
                "Multiple AKs — discuss field treatment with dermatologist"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = true,
            searchTags = listOf("actinic keratosis", "precancerous skin", "rough scaly patch", "sun damage precancer", "solar keratosis")
        ),

        SkinCondition(
            id = "dysplastic_nevus",
            name = "Abnormal Mole (Dysplastic Nevus)",
            category = "Dangerous",
            description = "A dysplastic nevus is an unusual-looking mole that may have some features similar to melanoma. Having dysplastic nevi increases the risk of melanoma. They are NOT melanoma themselves, but require monitoring and sometimes removal. Regular skin checks are essential.",
            symptoms = listOf(
                "Larger than a typical mole (often 5mm or more)",
                "Irregular or indistinct borders",
                "Uneven color (mix of shades of brown, pink, or tan)",
                "Flat or slightly raised surface",
                "May be multiple",
                "No pain or itching usually — changes are the warning sign"
            ),
            whoIsAffected = "About 10% of the general population has dysplastic nevi. Higher risk in people with many moles, fair skin, or family history of melanoma.",
            affectedAreas = listOf("Back (most common)", "Chest", "Arms", "Legs", "Scalp"),
            homeCareSteps = listOf(
                "Photograph your moles every 3-6 months to track changes",
                "Perform monthly self-checks following the ABCDE rule",
                "See a dermatologist annually (or more often if you have many moles)",
                "Apply SPF 50 sunscreen daily",
                "Avoid tanning beds"
            ),
            thingsToAvoid = listOf(
                "Tanning beds",
                "Sunburn",
                "Delaying medical evaluation of changing moles"
            ),
            otcTreatmentCategories = listOf(
                "NO OTC treatment — medical monitoring and possible surgical removal",
                "Prevention: SPF 50 sunscreen"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Any mole that suddenly changes, bleeds, or shows ABCDE features — see dermatologist urgently",
                "New dark spot in an unusual location (palm, sole, under nail)"
            ),
            doctorType = "Dermatologist — may need biopsy",
            isContagious = false,
            isDangerous = true,
            searchTags = listOf("dysplastic nevus", "abnormal mole", "atypical mole", "mole monitoring", "pre-melanoma mole")
        ),

        // ─── OTHER COMMON CONDITIONS ─────────────────────────────────────────────

        SkinCondition(
            id = "sebaceous_cyst",
            name = "Sebaceous Cyst",
            category = "Other",
            description = "A sebaceous cyst is a non-cancerous lump beneath the skin, filled with keratin protein (not sebum, despite the name). They are very common and usually harmless. They grow slowly and are typically painless unless infected. Most do not require treatment unless they become bothersome.",
            symptoms = listOf(
                "Round, firm lump under the skin",
                "Smooth, skin-colored or slightly yellowish surface",
                "Small dark plug (punctum) at the center in some cases",
                "Usually painless unless infected",
                "Infected cyst: red, warm, swollen, and very tender",
                "May discharge a thick, foul-smelling material"
            ),
            whoIsAffected = "Can affect anyone at any age. Most common in adults. More common in people with acne or previous skin injuries.",
            affectedAreas = listOf("Face", "Neck", "Trunk", "Back", "Genitals", "Any area with hair follicles"),
            homeCareSteps = listOf(
                "Do NOT squeeze or try to pop the cyst — causes infection and scarring",
                "If not infected, leave it alone — most don't need treatment",
                "Apply warm compresses if it becomes inflamed (NOT infected)",
                "See a doctor if it becomes red, painful, or oozing"
            ),
            thingsToAvoid = listOf(
                "Squeezing or popping the cyst — can cause infection or rupture",
                "Self-drainage with needles — infection risk"
            ),
            otcTreatmentCategories = listOf(
                "No OTC treatment — leave alone if asymptomatic",
                "Infected cysts require medical drainage and antibiotics"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Red, hot, rapidly enlarging cyst with pus — see doctor (infected cyst needs drainage)",
                "Cyst on eyelid causing vision problems — see doctor"
            ),
            doctorType = "General Practitioner or Dermatologist for removal if desired",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("sebaceous cyst", "cyst skin", "lump under skin", "skin bump", "keratin cyst", "epidermoid cyst")
        ),

        SkinCondition(
            id = "keloid",
            name = "Keloid Scar",
            category = "Other",
            description = "A keloid is an overgrowth of scar tissue that forms at a wound site and extends beyond the original injury boundary. Unlike normal scars, keloids don't stop growing and can become larger over months or years. They are not dangerous but can be itchy, tender, or cosmetically concerning.",
            symptoms = listOf(
                "Thick, raised scar that grows beyond the original wound boundary",
                "Smooth, shiny, firm texture",
                "Pink, red, or darker than surrounding skin",
                "Itchy or occasionally painful",
                "Continues to grow slowly over months to years",
                "Can restrict movement near joints if large"
            ),
            whoIsAffected = "Much more common in people with darker skin tones (African, Caribbean, South Asian origin). Age group 10-30 most affected. Genetic predisposition plays a role.",
            affectedAreas = listOf("Earlobes (from piercing)", "Chest/sternum", "Shoulders", "Upper back", "Any wound or incision site"),
            homeCareSteps = listOf(
                "Silicone gel sheets applied daily may flatten and soften keloids over time (6+ months)",
                "Pressure garments can help prevent or treat keloids",
                "Massage the scar with oil gently twice daily",
                "Protect from sun — pigmentation can worsen",
                "See a dermatologist for treatment options (steroid injections, laser)"
            ),
            thingsToAvoid = listOf(
                "Unnecessary piercings or surgeries if prone to keloids",
                "Tight clothing or jewelry over keloids"
            ),
            otcTreatmentCategories = listOf(
                "Silicone gel sheets or silicone gel",
                "Scar treatment gels"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "No emergencies — but see a dermatologist if it restricts movement or becomes very uncomfortable"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("keloid", "raised scar", "overgrown scar", "scar tissue", "thick scar")
        ),

        SkinCondition(
            id = "dry_skin_xerosis",
            name = "Dry Skin (Xerosis)",
            category = "Other",
            description = "Dry skin (xerosis) is a very common condition where the skin lacks sufficient moisture. It can cause itching, cracking, and a rough texture. For most people it is a temporary, seasonal problem, but it can be chronic in older adults or people with certain medical conditions like diabetes.",
            symptoms = listOf(
                "Tight, uncomfortable feeling of skin",
                "Rough, dull skin texture",
                "Slight to severe flaking, scaling, or peeling",
                "Itching (pruritus)",
                "Fine lines or cracks in the skin",
                "Deep fissures (cracks) that may bleed in severe cases",
                "Redness"
            ),
            whoIsAffected = "Very common — affects people of all ages. Worsens in cold, dry weather. More common in older adults as skin produces less oil with age.",
            affectedAreas = listOf("Hands", "Arms", "Lower legs", "Ankles", "Feet", "Anywhere — especially in winter"),
            homeCareSteps = listOf(
                "Moisturize skin within 3 minutes after bathing — seals in water",
                "Use thick creams or ointments (better than lotions for dry skin)",
                "Use lukewarm water (not hot) for baths and showers",
                "Keep baths and showers short (5-10 minutes)",
                "Use mild, fragrance-free soap",
                "Use a humidifier indoors during dry winter months",
                "Drink adequate water",
                "Wear soft, breathable clothing next to skin"
            ),
            thingsToAvoid = listOf(
                "Hot showers or baths",
                "Harsh soaps or detergents",
                "Scratching dry skin",
                "Sitting too close to heaters or fires",
                "Wet clothing left on skin"
            ),
            otcTreatmentCategories = listOf(
                "Thick, fragrance-free emollient creams (ceramide based preferred)",
                "Petroleum jelly for severely dry areas",
                "Urea-based creams for very rough skin"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "Severe, widespread xerosis with no improvement after moisturizing — may indicate thyroid or kidney disease",
                "Deep bleeding cracks on feet — infection risk, see a doctor"
            ),
            doctorType = "General Practitioner or Dermatologist if persistent",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("dry skin", "xerosis", "skin cracking", "flaky skin", "itchy dry skin", "moisturizer")
        ),

        SkinCondition(
            id = "sunburn",
            name = "Sunburn",
            category = "Other",
            description = "Sunburn is skin damage caused by ultraviolet (UV) radiation from the sun or tanning beds. It can range from mild redness to severe blistering. Every sunburn damages skin cells and increases lifetime risk of skin cancer. Prevention with sunscreen and protective clothing is essential.",
            symptoms = listOf(
                "Red, warm, tender, or painful skin",
                "Skin that feels hot to the touch",
                "Blistering in severe burns",
                "Peeling skin as it heals (1 week after burn)",
                "Itching as it heals",
                "Headache, fever, nausea with severe sunburn (sun poisoning)"
            ),
            whoIsAffected = "Anyone who spends time in the sun without protection. Fair skin burns faster. Children are particularly vulnerable.",
            affectedAreas = listOf("Any sun-exposed area", "Shoulders", "Face", "Back", "Arms"),
            homeCareSteps = listOf(
                "Get out of the sun immediately",
                "Cool the skin with cool (not ice cold) water or cool wet compresses",
                "Apply aloe vera gel or an aftersun lotion to soothe",
                "Take paracetamol or ibuprofen for pain and inflammation",
                "Drink plenty of water — sunburn draws fluid to the skin surface",
                "Do not burst blisters — this increases infection risk",
                "Keep out of the sun until fully healed"
            ),
            thingsToAvoid = listOf(
                "Further sun exposure until healed",
                "Ice directly on skin (causes frostbite)",
                "Butter, toothpaste, or other home remedies",
                "Popping blisters",
                "Tight clothing over burned areas"
            ),
            otcTreatmentCategories = listOf(
                "Aloe vera gel (soothing)",
                "Aftersun lotions (soothing and moisturizing)",
                "Pain relief: paracetamol or ibuprofen",
                "Hydrocortisone cream for mild inflammation (short-term)"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "High fever (above 39°C / 102°F), confusion, or severe headache — seek emergency care (heat stroke)",
                "Extensive blistering over large body area — medical evaluation needed",
                "Signs of infection in blisters: pus, spreading redness, fever"
            ),
            doctorType = "Emergency Room for severe cases, General Practitioner for persistent pain or infection",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("sunburn", "UV damage", "skin burn", "red skin sun", "peeling skin", "sun poisoning")
        ),

        SkinCondition(
            id = "hidradenitis_suppurativa",
            name = "Hidradenitis Suppurativa",
            category = "Inflammatory",
            description = "Hidradenitis suppurativa (HS) is a chronic, painful skin condition that causes lumps, abscesses, and tunnels in the skin, particularly in areas where skin rubs together. It starts around hair follicles and can be very debilitating. It is not caused by poor hygiene and is not contagious. Early treatment improves outcomes significantly.",
            symptoms = listOf(
                "Painful, pea-sized lumps in armpits, groin, inner thighs, under breasts",
                "Lumps that may burst and drain pus or blood",
                "Blackheads in pairs (double-ended comedones) — characteristic sign",
                "Tunnels (sinus tracts) connecting bumps under the skin",
                "Scarring as lesions heal",
                "Condition cycles through flares and remissions"
            ),
            whoIsAffected = "Affects about 1% of the population. More common in women, typically starts after puberty. Obesity, smoking, and family history increase risk.",
            affectedAreas = listOf("Armpits", "Groin", "Inner thighs", "Under breasts", "Buttocks", "Around genitals"),
            homeCareSteps = listOf(
                "See a doctor or dermatologist for a treatment plan",
                "Keep affected areas clean and dry",
                "Wear loose, breathable clothing to reduce friction",
                "Avoid shaving affected areas",
                "Maintain a healthy weight — can improve symptoms",
                "Stop smoking — significantly worsens HS",
                "Apply warm compresses to painful nodules for temporary relief",
                "Avoid anti-perspirants on affected areas if they irritate"
            ),
            thingsToAvoid = listOf(
                "Smoking",
                "Tight, synthetic clothing in affected areas",
                "Squeezing or popping the lumps — increases risk of tunneling",
                "Shaving affected areas"
            ),
            otcTreatmentCategories = listOf(
                "Antibacterial soap for gentle cleansing",
                "Medical treatment is essential — prescription antibiotics, biologics, or surgery"
            ),
            urgencyLevel = UrgencyLevel.MEDIUM,
            emergencyWarnings = listOf(
                "Rapidly spreading, extremely painful abscesses with fever — urgent medical care",
                "HS affecting daily life significantly — dermatologist referral essential"
            ),
            doctorType = "Dermatologist",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("hidradenitis suppurativa", "HS", "armpit lumps", "groin lumps", "recurring boils", "skin tunnels")
        ),

        SkinCondition(
            id = "milia",
            name = "Milia (Milk Spots)",
            category = "Other",
            description = "Milia are small, white cysts that appear on the skin when keratin (a protein) becomes trapped beneath the surface. They look like small white pearls. They are very common in newborns and also occur in adults. They are harmless and usually resolve on their own.",
            symptoms = listOf(
                "Small (1-2mm), firm, white or yellowish bumps",
                "No redness, pain, or itching",
                "Most visible under the eyes, on nose and cheeks",
                "In newborns: appear on nose, cheeks, chin, forehead — disappear within weeks",
                "In adults: may persist for months if untreated"
            ),
            whoIsAffected = "Very common in newborns (about 50%). Also occurs in adults, especially after skin injury, burns, or use of heavy creams.",
            affectedAreas = listOf("Under eyes", "Nose", "Cheeks", "Forehead", "Chin"),
            homeCareSteps = listOf(
                "In newborns: leave alone — they disappear on their own within a few weeks",
                "In adults: wash face regularly with gentle cleanser and exfoliate gently",
                "Avoid heavy, pore-blocking face creams",
                "Exfoliate with mild chemical exfoliant (glycolic acid cleanser) to help remove over time",
                "Do NOT try to squeeze or pop milia — can cause scarring"
            ),
            thingsToAvoid = listOf(
                "Squeezing or picking milia",
                "Heavy, occlusive face creams that clog pores"
            ),
            otcTreatmentCategories = listOf(
                "Gentle glycolic acid cleansers",
                "Professional extraction by dermatologist/aesthetician if persistent"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "No emergencies — completely benign"
            ),
            doctorType = "Dermatologist (optional — for cosmetic removal if desired)",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("milia", "milk spots", "white bumps skin", "under eye bumps", "newborn white spots")
        ),

        SkinCondition(
            id = "cherry_angioma",
            name = "Cherry Angioma",
            category = "Other",
            description = "Cherry angiomas are small, bright red or purple growths made up of dilated blood vessels near the skin's surface. They are extremely common and completely benign (non-cancerous). They tend to appear after age 30 and become more numerous with age. No treatment is needed unless they bleed or are cosmetically concerning.",
            symptoms = listOf(
                "Small (1-5mm), round, bright red or cherry-red dome-shaped bump",
                "Smooth surface",
                "May appear purple if bruised or scraped",
                "Completely painless",
                "Multiple may appear over time",
                "Bleed easily if scratched or cut"
            ),
            whoIsAffected = "Very common in adults over 30. Nearly all people will develop some by age 70. No known cause — likely genetic and hormonal.",
            affectedAreas = listOf("Trunk (most common)", "Arms", "Shoulders", "Any body surface"),
            homeCareSteps = listOf(
                "Leave them alone — they require no treatment",
                "If one bleeds, apply gentle pressure",
                "See a doctor if a spot changes in appearance, ulcerates, or is uncertain"
            ),
            thingsToAvoid = listOf(
                "Scratching or picking (they bleed easily)"
            ),
            otcTreatmentCategories = listOf(
                "No treatment needed — cosmetic removal by dermatologist if desired"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "A 'cherry angioma' that bleeds without cause, grows rapidly, or is not perfectly red and round — see dermatologist to rule out other conditions"
            ),
            doctorType = "Dermatologist (optional — cosmetic removal only)",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("cherry angioma", "red spots skin", "blood spots skin", "cherry spot", "red mole", "red dot skin")
        ),

        SkinCondition(
            id = "skin_tag",
            name = "Skin Tag (Acrochordon)",
            category = "Other",
            description = "Skin tags are small, soft, flesh-colored growths that hang off the skin on a thin piece of tissue called a stalk. They are extremely common and harmless. They typically cause no symptoms unless they catch on clothing or jewelry. No treatment is needed, but they can be easily removed for cosmetic reasons.",
            symptoms = listOf(
                "Small, soft, flesh-colored or slightly darker flap of skin",
                "Attached to skin by a thin stalk",
                "Smooth or slightly irregular surface",
                "Usually 2-5mm in size",
                "Painless (unless caught on clothing or jewelry)",
                "May become irritated if in a rubbing area"
            ),
            whoIsAffected = "Extremely common — about 46% of people have them. More common with age, weight gain, pregnancy, and diabetes.",
            affectedAreas = listOf("Neck", "Armpits", "Groin", "Eyelids", "Under breasts", "Any skin fold area"),
            homeCareSteps = listOf(
                "Leave them alone — they are harmless",
                "If irritated, protect with a bandage or soft padding",
                "See a doctor for removal if desired (do not try to remove at home)"
            ),
            thingsToAvoid = listOf(
                "Attempting home removal (cutting, tying off) without medical guidance — risk of infection and bleeding"
            ),
            otcTreatmentCategories = listOf(
                "OTC skin tag removal kits (cryotherapy type) available but medical removal is safer",
                "Professional removal: snipping, cryotherapy, or electrocautery by doctor"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "If a 'skin tag' grows rapidly, bleeds spontaneously, or looks unusual — see dermatologist to confirm it is not something else"
            ),
            doctorType = "General Practitioner or Dermatologist (optional — cosmetic removal)",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("skin tag", "acrochordon", "flap skin", "small hanging skin", "flesh colored bump")
        ),

        SkinCondition(
            id = "normal_healthy_skin",
            name = "Normal Healthy Skin",
            category = "Normal",
            description = "The analysis suggests this may be normal, healthy skin with no visible signs of a skin condition. Skin varies greatly between individuals in color, texture, and appearance. If you have concerns about your skin, a dermatologist can provide a thorough evaluation.",
            symptoms = listOf(
                "No visible signs of rash, infection, or unusual growths",
                "Consistent skin texture and color",
                "No persistent redness, swelling, or scaling"
            ),
            whoIsAffected = "This is a baseline — all people have healthy skin that can look different based on skin tone, age, and environment.",
            affectedAreas = listOf("Any body area"),
            homeCareSteps = listOf(
                "Maintain daily moisturizing routine",
                "Apply SPF 30+ sunscreen daily on exposed areas",
                "Perform monthly skin self-checks for any unusual changes",
                "Stay hydrated and eat a balanced diet",
                "See a dermatologist annually for a professional skin check"
            ),
            thingsToAvoid = listOf(
                "Excessive sun exposure without protection",
                "Smoking — accelerates skin aging",
                "Harsh soaps or skincare products"
            ),
            otcTreatmentCategories = listOf(
                "Gentle, fragrance-free moisturizer",
                "Daily SPF 30+ sunscreen"
            ),
            urgencyLevel = UrgencyLevel.LOW,
            emergencyWarnings = listOf(
                "If you are still concerned about a skin change, please see a dermatologist for professional evaluation"
            ),
            doctorType = "Dermatologist (for annual skin check)",
            isContagious = false,
            isDangerous = false,
            searchTags = listOf("normal skin", "healthy skin", "no condition")
        )
    )

    /**
     * Find a condition by its ID.
     * Used when the ML model returns a result — we look up the full condition info.
     */
    fun getConditionById(id: String): SkinCondition? {
        return getAllConditions().find { it.id == id }
    }

    /**
     * Get all conditions filtered by category.
     * Used by the Library screen category filters.
     */
    fun getConditionsByCategory(category: String): List<SkinCondition> {
        return getAllConditions().filter { it.category.equals(category, ignoreCase = true) }
    }

    /**
     * Search conditions by name, description, or search tags.
     * Used by the Library search bar.
     */
    fun searchConditions(query: String): List<SkinCondition> {
        if (query.isBlank()) return getAllConditions()
        val lowerQuery = query.lowercase()
        return getAllConditions().filter { condition ->
            condition.name.lowercase().contains(lowerQuery) ||
            condition.description.lowercase().contains(lowerQuery) ||
            condition.searchTags.any { tag -> tag.lowercase().contains(lowerQuery) }
        }
    }

    /**
     * Get all unique categories.
     * Used by the Library screen to build category chips.
     */
    fun getAllCategories(): List<String> {
        return listOf("All", "Infection", "Inflammatory", "Pigmentation", "Dangerous", "Other")
    }
}
