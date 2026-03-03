package ai.saifullah.dermaware.data.network

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import ai.saifullah.dermaware.data.model.ConditionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Singleton

/**
 * Sends skin photos to Gemini 2.0 Flash via OpenRouter API for analysis.
 *
 * HOW IT WORKS:
 * 1. Takes a photo (Bitmap) and the body area the user selected
 * 2. Converts the bitmap to base64 JPEG
 * 3. Sends it to OpenRouter (which routes to Gemini 2.0 Flash)
 * 4. Parses the JSON response into ConditionResult objects
 *
 * FALLBACK: If no API key or call fails, the app falls back to TFLite.
 */
@Singleton
class GeminiService(
    private val apiKey: String
) {
    companion object {
        private const val TAG = "DermAware.AI"
        private const val OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions"
        // Gemini 2.0 Flash via OpenRouter — same model, works worldwide
        private const val MODEL = "google/gemini-2.0-flash-001"

        // All condition IDs from the SkinConditionDatabase — AI must pick from these
        // so the results map correctly to our offline condition detail pages
        private val KNOWN_CONDITION_IDS = listOf(
            "tinea_corporis", "tinea_pedis", "tinea_capitis", "tinea_unguium",
            "scabies", "impetigo", "cellulitis", "folliculitis",
            "molluscum_contagiosum", "warts_verruca", "herpes_simplex", "candidiasis",
            "atopic_dermatitis", "psoriasis", "seborrheic_dermatitis", "contact_dermatitis",
            "rosacea", "acne_vulgaris", "lichen_planus", "pityriasis_rosea",
            "urticaria", "perioral_dermatitis",
            "vitiligo", "melasma", "pityriasis_versicolor", "post_inflammatory_hyperpigmentation",
            "melanoma", "basal_cell_carcinoma", "squamous_cell_carcinoma",
            "actinic_keratosis", "dysplastic_nevus",
            "sebaceous_cyst", "keloid", "dry_skin_xerosis", "sunburn",
            "hidradenitis_suppurativa", "milia", "cherry_angioma", "skin_tag",
            "normal_healthy_skin"
        )
    }

    /**
     * Returns true if the OpenRouter API key is configured.
     * When false, the app should use TFLite instead.
     */
    fun isAvailable(): Boolean {
        val available = apiKey.isNotBlank()
        Log.d(TAG, "isAvailable() = $available")
        return available
    }

    /**
     * Analyze a skin photo using Gemini 2.0 Flash via OpenRouter.
     *
     * @param bitmap The photo to analyze (already loaded into memory)
     * @param bodyArea The body area the user selected (e.g., "face", "arm", "leg")
     * @return GeminiResult with conditions + summary, or null if anything fails
     */
    suspend fun analyze(bitmap: Bitmap, bodyArea: String): GeminiResult? {
        if (apiKey.isBlank()) return null

        return try {
            Log.d(TAG, "Starting analysis for body area: $bodyArea, image: ${bitmap.width}x${bitmap.height}")

            // Convert bitmap to base64 JPEG (resized if too large to save bandwidth)
            val base64Image = withContext(Dispatchers.Default) { bitmapToBase64(bitmap) }
            Log.d(TAG, "Image encoded to base64, length: ${base64Image.length}")

            // Build the JSON request body for OpenRouter
            val prompt = buildPrompt(bodyArea)
            val requestBody = buildRequestBody(prompt, base64Image)

            // Send HTTP request to OpenRouter and get raw response
            val rawResponse = withContext(Dispatchers.IO) { sendRequest(requestBody) }
            if (rawResponse == null) return null

            Log.d(TAG, "Raw API response: ${rawResponse.take(500)}")

            // Extract the AI's text content from the OpenRouter response format
            val aiContent = extractContent(rawResponse)
            if (aiContent == null) {
                Log.e(TAG, "Could not extract content from response")
                return null
            }

            Log.d(TAG, "AI content: $aiContent")

            // Parse the JSON that the AI returned into our data objects
            parseResponse(aiContent)
        } catch (e: Exception) {
            Log.e(TAG, "Analysis failed: ${e.javaClass.simpleName}: ${e.message}", e)
            null
        }
    }

    /**
     * Convert a Bitmap to a base64-encoded JPEG string.
     * Resizes large images to max 1024px on longest side to save bandwidth.
     */
    private fun bitmapToBase64(bitmap: Bitmap): String {
        // Resize if image is too large (saves bandwidth and speeds up API call)
        val maxDim = 1024
        val scaledBitmap = if (bitmap.width > maxDim || bitmap.height > maxDim) {
            val scale = maxDim.toFloat() / maxOf(bitmap.width, bitmap.height)
            Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * scale).toInt(),
                (bitmap.height * scale).toInt(),
                true
            )
        } else {
            bitmap
        }

        val stream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }

    /**
     * Build the JSON request body in OpenAI-compatible format (used by OpenRouter).
     * Includes the text prompt and the base64-encoded image.
     */
    private fun buildRequestBody(prompt: String, base64Image: String): String {
        // Build the multimodal message with text + image
        val contentArray = JSONArray().apply {
            // Text part — the dermatology prompt
            put(JSONObject().apply {
                put("type", "text")
                put("text", prompt)
            })
            // Image part — base64 JPEG encoded
            put(JSONObject().apply {
                put("type", "image_url")
                put("image_url", JSONObject().apply {
                    put("url", "data:image/jpeg;base64,$base64Image")
                })
            })
        }

        val messages = JSONArray().put(
            JSONObject().apply {
                put("role", "user")
                put("content", contentArray)
            }
        )

        return JSONObject().apply {
            put("model", MODEL)
            put("messages", messages)
            put("temperature", 0.3)
            put("max_tokens", 1024)
        }.toString()
    }

    /**
     * Send the HTTP POST request to OpenRouter API.
     * Returns the raw JSON response string, or null on failure.
     */
    private fun sendRequest(body: String): String? {
        val url = URL(OPENROUTER_URL)
        val connection = url.openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            // OpenRouter requires these headers for identification
            connection.setRequestProperty("HTTP-Referer", "https://dermaware.saifullah.ai")
            connection.setRequestProperty("X-Title", "DermAware")
            connection.doOutput = true
            connection.connectTimeout = 30000   // 30 seconds to connect
            connection.readTimeout = 60000      // 60 seconds to read response

            // Write the JSON body
            connection.outputStream.bufferedWriter().use { it.write(body) }

            val responseCode = connection.responseCode
            Log.d(TAG, "HTTP response code: $responseCode")

            if (responseCode == 200) {
                // Success — read the response
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                // Error — read error body for logging
                val errorBody = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "no error body"
                Log.e(TAG, "API error ($responseCode): $errorBody")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "HTTP request failed: ${e.message}", e)
            null
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Extract the AI's text content from OpenRouter's response format.
     * OpenRouter uses the same format as OpenAI: response.choices[0].message.content
     */
    private fun extractContent(responseText: String): String? {
        return try {
            val json = JSONObject(responseText)
            json.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to extract content from response: ${e.message}")
            null
        }
    }

    /**
     * Build the structured prompt for the AI.
     * Includes the body area context and the full list of known condition IDs
     * so the AI maps its findings to our database.
     */
    private fun buildPrompt(bodyArea: String): String {
        val conditionIdList = KNOWN_CONDITION_IDS.joinToString(", ")

        return """You are a dermatology analysis assistant providing EDUCATIONAL information only (NOT medical diagnosis).
Analyze the provided skin photo taken on the $bodyArea.

Identify up to 3 possible skin conditions that match what you see.
For conditionId, use ONLY IDs from this list when applicable:
[$conditionIdList]

If you cannot identify a condition or it appears to be normal skin, use "normal_healthy_skin".
If the image is not a skin photo or is unclear, still return valid JSON with "normal_healthy_skin" and a low confidence.

Assign confidence scores between 0.0 and 1.0 based on visual certainty.
For category, use one of: Infection, Inflammatory, Pigmentation, Dangerous, Other.

IMPORTANT: Return ONLY a valid JSON object, no markdown, no code fences, no extra text.
Use this exact format:
{"conditions":[{"conditionId":"example_id","displayName":"Example Name","confidence":0.85,"category":"Inflammatory"}],"summary":"1-2 sentence educational observation about what is visible in the photo."}"""
    }

    /**
     * Parse the AI's JSON response into a GeminiResult.
     * Returns null if the JSON is malformed or missing required fields.
     */
    private fun parseResponse(responseText: String): GeminiResult? {
        return try {
            // Clean up response — remove markdown code fences if AI adds them
            val cleanText = responseText
                .trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            Log.d(TAG, "Parsing cleaned response: $cleanText")

            val json = JSONObject(cleanText)

            // Parse the conditions array
            val conditionsArray = json.getJSONArray("conditions")
            val conditions = mutableListOf<ConditionResult>()

            for (i in 0 until conditionsArray.length()) {
                val condObj = conditionsArray.getJSONObject(i)
                conditions.add(
                    ConditionResult(
                        conditionId = condObj.getString("conditionId"),
                        displayName = condObj.getString("displayName"),
                        confidence = condObj.getDouble("confidence").toFloat(),
                        category = condObj.optString("category", "Other")
                    )
                )
            }

            // Sort by confidence (highest first) and take top 3
            val topConditions = conditions
                .sortedByDescending { it.confidence }
                .take(3)

            // Parse the summary text
            val summary = json.optString("summary", "")

            Log.d(TAG, "Parsed ${topConditions.size} conditions, summary length: ${summary.length}")

            GeminiResult(
                conditions = topConditions,
                summary = summary
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse AI response: ${e.message}", e)
            null
        }
    }

    /**
     * Holds the parsed result from an AI analysis.
     */
    data class GeminiResult(
        // Top conditions identified, using the same ConditionResult format as TFLite
        val conditions: List<ConditionResult>,
        // 1-2 sentence educational observation from the AI
        val summary: String
    )
}
