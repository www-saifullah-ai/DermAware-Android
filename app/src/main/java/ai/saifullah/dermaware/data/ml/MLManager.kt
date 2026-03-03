package ai.saifullah.dermaware.data.ml

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import ai.saifullah.dermaware.data.model.ConditionResult
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the TFLite model for on-device skin condition analysis.
 *
 * HOW IT WORKS:
 * 1. The model file (dermaware_model.tflite) is loaded from app assets
 * 2. A photo is preprocessed into 224x224 normalized float pixels
 * 3. The model runs inference and outputs confidence scores for each condition
 * 4. We return the top 3 results with confidence above 15%
 *
 * SETUP: To use the real model, drop dermaware_model.tflite into:
 *   app/src/main/assets/ml/dermaware_model.tflite
 * The labels.txt file in the same folder maps output indices to condition names.
 */
@Singleton
class MLManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val TAG = "DermAware.MLManager"
        private const val MODEL_FILE = "ml/dermaware_model.tflite"
        private const val LABELS_FILE = "ml/labels.txt"
        // Only show results with at least 15% confidence
        private const val MIN_CONFIDENCE_THRESHOLD = 0.15f
        // Return at most 3 results
        private const val TOP_K_RESULTS = 3
    }

    // The TFLite interpreter — holds the loaded model in memory
    private var interpreter: Interpreter? = null
    // List of condition labels parsed from labels.txt
    private var labels: List<LabelEntry> = emptyList()
    // Whether the model was successfully loaded
    private var isModelAvailable = false

    /**
     * Represents one parsed line from labels.txt.
     * Format: technical_id|Display Name|Category
     */
    data class LabelEntry(
        val id: String,
        val displayName: String,
        val category: String
    )

    /**
     * Initialize the model — must be called before running inference.
     * Called lazily when the user first tries to analyze a photo.
     */
    suspend fun initialize(): ModelInitResult = withContext(Dispatchers.IO) {
        return@withContext try {
            // Load labels first — needed to parse model output
            labels = loadLabels()
            if (labels.isEmpty()) {
                return@withContext ModelInitResult.Error("Labels file not found or empty")
            }

            // Try to load the TFLite model
            val modelBuffer = loadModelFile()
            val options = Interpreter.Options().apply {
                // Use 2 threads for faster inference on multi-core devices
                numThreads = 2
            }
            interpreter = Interpreter(modelBuffer, options)
            isModelAvailable = true
            Log.d(TAG, "Model loaded successfully. Labels count: ${labels.size}")
            ModelInitResult.Success
        } catch (e: IOException) {
            // Model file not found — app works without it, just shows "model not available" message
            Log.w(TAG, "TFLite model not found at assets/$MODEL_FILE. Running in demo mode.")
            isModelAvailable = false
            ModelInitResult.ModelNotFound
        } catch (e: Exception) {
            Log.e(TAG, "Error loading model: ${e.message}", e)
            isModelAvailable = false
            ModelInitResult.Error(e.message ?: "Unknown error loading model")
        }
    }

    /**
     * Run inference on a skin photo and return the top matching conditions.
     *
     * @param imageUri URI of the photo to analyze
     * @return InferenceResult with top conditions or an error message
     */
    suspend fun analyze(imageUri: Uri): InferenceResult = withContext(Dispatchers.IO) {
        // If the model file isn't available, return demo/sample results
        if (!isModelAvailable) {
            return@withContext getDemoResults()
        }

        // Safety net: if interpreter was closed but isModelAvailable is still true,
        // try to re-initialize before giving up
        if (interpreter == null) {
            Log.w(TAG, "Interpreter was null despite isModelAvailable=true. Re-initializing...")
            initialize()
        }

        val localInterpreter = interpreter
            ?: return@withContext InferenceResult.Error("Model not initialized. Please restart the app.")

        return@withContext try {
            // Step 1: Preprocess the image into the model's expected format
            val preprocessor = ImagePreprocessor()
            val inputBuffer = preprocessor.preprocessUri(context, imageUri)

            // Step 2: Prepare the output array — one confidence score per condition
            val outputArray = Array(1) { FloatArray(labels.size) }

            // Step 3: Run inference — this is the core ML computation
            localInterpreter.run(inputBuffer, outputArray)

            // Step 4: Parse the output into human-readable results
            val confidences = outputArray[0]
            val results = parseResults(confidences)

            if (results.isEmpty()) {
                InferenceResult.NoMatch("No conditions matched with sufficient confidence. Try a clearer, better-lit photo.")
            } else {
                InferenceResult.Success(results)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Inference error: ${e.message}", e)
            InferenceResult.Error("Analysis failed: ${e.message}")
        }
    }

    /**
     * Apply softmax to convert raw model logits into probabilities (0.0 to 1.0).
     * The model outputs raw scores (logits), so we need softmax to get
     * meaningful confidence percentages: softmax(x_i) = exp(x_i) / sum(exp(x_j))
     */
    private fun softmax(logits: FloatArray): FloatArray {
        // Subtract max for numerical stability (prevents overflow in exp)
        val max = logits.max()
        val exps = FloatArray(logits.size) { kotlin.math.exp((logits[it] - max).toDouble()).toFloat() }
        val sum = exps.sum()
        return FloatArray(exps.size) { exps[it] / sum }
    }

    /**
     * Convert raw model output into a sorted list of results.
     * Applies softmax to convert logits to probabilities, then filters
     * by minimum confidence threshold. Returns at most TOP_K_RESULTS (3) conditions.
     */
    private fun parseResults(confidences: FloatArray): List<ConditionResult> {
        // Convert raw logits to probabilities using softmax
        val probabilities = softmax(confidences)

        return probabilities
            .mapIndexed { index, confidence ->
                Pair(index, confidence)
            }
            .filter { (_, confidence) ->
                confidence >= MIN_CONFIDENCE_THRESHOLD
            }
            .sortedByDescending { (_, confidence) ->
                confidence
            }
            .take(TOP_K_RESULTS)
            .mapNotNull { (index, confidence) ->
                if (index < labels.size) {
                    val label = labels[index]
                    ConditionResult(
                        conditionId = label.id,
                        displayName = label.displayName,
                        confidence = confidence,
                        category = label.category
                    )
                } else null
            }
    }

    /**
     * Load the model file from app assets into a MappedByteBuffer.
     * Memory-mapped loading is efficient for large model files.
     */
    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    /**
     * Parse the labels.txt file from assets.
     * Each line format: technical_id|Display Name|Category
     * Lines starting with # are comments and are skipped.
     */
    private fun loadLabels(): List<LabelEntry> {
        return try {
            context.assets.open(LABELS_FILE).bufferedReader().readLines()
                .filter { line -> line.isNotBlank() && !line.startsWith("#") }
                .map { line ->
                    val parts = line.split("|")
                    LabelEntry(
                        id = parts.getOrElse(0) { "unknown" }.trim(),
                        displayName = parts.getOrElse(1) { "Unknown Condition" }.trim(),
                        category = parts.getOrElse(2) { "Other" }.trim()
                    )
                }
        } catch (e: IOException) {
            Log.e(TAG, "Could not load labels file: ${e.message}")
            emptyList()
        }
    }

    /**
     * Returns sample/demo results when no TFLite model file is present.
     * This allows the app to be fully demonstrated without the actual model.
     * Shows a clear note that the model is in demo mode.
     */
    private fun getDemoResults(): InferenceResult.Success {
        return InferenceResult.Success(
            listOf(
                ConditionResult(
                    conditionId = "atopic_dermatitis",
                    displayName = "Eczema (Atopic Dermatitis)",
                    confidence = 0.72f,
                    category = "Inflammatory"
                ),
                ConditionResult(
                    conditionId = "contact_dermatitis",
                    displayName = "Contact Dermatitis",
                    confidence = 0.41f,
                    category = "Inflammatory"
                ),
                ConditionResult(
                    conditionId = "psoriasis",
                    displayName = "Psoriasis",
                    confidence = 0.23f,
                    category = "Inflammatory"
                )
            ),
            isDemoMode = true
        )
    }

    /**
     * Free the model from memory when it's no longer needed.
     * Important on low-memory devices to prevent OutOfMemoryError.
     */
    fun close() {
        interpreter?.close()
        interpreter = null
        isModelAvailable = false
    }

    // Sealed class to represent all possible outcomes of model initialization
    sealed class ModelInitResult {
        object Success : ModelInitResult()
        object ModelNotFound : ModelInitResult()  // Model file not in assets
        data class Error(val message: String) : ModelInitResult()
    }

    // Sealed class to represent all possible outcomes of running inference
    sealed class InferenceResult {
        data class Success(
            val results: List<ConditionResult>,
            val isDemoMode: Boolean = false  // True if model file wasn't available
        ) : InferenceResult()

        data class NoMatch(val message: String) : InferenceResult()
        data class Error(val message: String) : InferenceResult()
    }
}
