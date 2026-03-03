package ai.saifullah.dermaware.ui.screen.analyze

import android.app.Application
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.saifullah.dermaware.data.database.entity.AnalysisResult
import ai.saifullah.dermaware.data.ml.MLManager
import ai.saifullah.dermaware.data.model.PhotoAnalysisSession
import ai.saifullah.dermaware.data.network.GeminiService
import ai.saifullah.dermaware.data.repository.AnalysisRepository
import ai.saifullah.dermaware.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the photo analysis flow (Analyze screen and Results screen).
 * Handles: model initialization, running inference on a selected photo,
 * saving results to the database.
 *
 * ANALYSIS PRIORITY:
 * 1. If Gemini API key is set AND device is online → use Gemini (better accuracy)
 * 2. If Gemini fails → fall back to TFLite automatically
 * 3. If no API key OR no internet → use TFLite directly
 */
@HiltViewModel
class AnalyzeViewModel @Inject constructor(
    private val application: Application,
    private val mlManager: MLManager,
    private val geminiService: GeminiService,
    private val analysisRepository: AnalysisRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    companion object {
        private const val TAG = "DermAware.AnalyzeVM"
    }

    // Represents all possible states of the analysis UI
    sealed class AnalysisState {
        object Idle : AnalysisState()                          // Waiting for user to pick a photo
        object InitializingModel : AnalysisState()             // Loading the TFLite model
        object AnalyzingPhoto : AnalysisState()                // Running ML inference
        data class Results(val session: PhotoAnalysisSession) : AnalysisState()  // Analysis done
        data class Error(val message: String) : AnalysisState()
    }

    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState.asStateFlow()

    // The selected body area (user picks before analyzing)
    private val _selectedBodyArea = MutableStateFlow("face")
    val selectedBodyArea: StateFlow<String> = _selectedBodyArea.asStateFlow()

    // The photo URI selected by the user
    private val _selectedPhotoUri = MutableStateFlow<Uri?>(null)
    val selectedPhotoUri: StateFlow<Uri?> = _selectedPhotoUri.asStateFlow()

    // Whether the model is in demo mode (no real model file available)
    private val _isDemoMode = MutableStateFlow(false)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode.asStateFlow()

    // ID of the saved result — used to navigate to results screen after saving
    private val _savedResultId = MutableStateFlow<Long?>(null)
    val savedResultId: StateFlow<Long?> = _savedResultId.asStateFlow()

    // Flag to prevent the LaunchedEffect from re-navigating after the user presses back
    private val _hasNavigatedToResults = MutableStateFlow(false)
    val hasNavigatedToResults: StateFlow<Boolean> = _hasNavigatedToResults.asStateFlow()

    fun markResultsNavigated() {
        _hasNavigatedToResults.value = true
    }

    init {
        // Initialize the TFLite model when ViewModel is created
        initializeModel()
    }

    private fun initializeModel() {
        viewModelScope.launch {
            _analysisState.value = AnalysisState.InitializingModel
            when (val result = mlManager.initialize()) {
                is MLManager.ModelInitResult.Success -> {
                    _analysisState.value = AnalysisState.Idle
                    _isDemoMode.value = false
                }
                is MLManager.ModelInitResult.ModelNotFound -> {
                    _analysisState.value = AnalysisState.Idle
                    _isDemoMode.value = true  // Will use demo results
                }
                is MLManager.ModelInitResult.Error -> {
                    _analysisState.value = AnalysisState.Error(result.message)
                }
            }
        }
    }

    fun setBodyArea(bodyArea: String) {
        _selectedBodyArea.value = bodyArea
    }

    fun setPhotoUri(uri: Uri) {
        _selectedPhotoUri.value = uri
    }

    /**
     * Check if the device currently has an active internet connection.
     * Uses ConnectivityManager to check for Wi-Fi, cellular, or ethernet.
     */
    private fun isOnline(): Boolean {
        val connectivityManager = application.getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Run analysis on the selected photo.
     * Tries Gemini first (if available + online), then falls back to TFLite.
     */
    fun analyzePhoto() {
        // Reset navigation flag so a new analysis can trigger navigation
        _hasNavigatedToResults.value = false
        _savedResultId.value = null

        val photoUri = _selectedPhotoUri.value ?: run {
            _analysisState.value = AnalysisState.Error("No photo selected")
            return
        }

        viewModelScope.launch {
            _analysisState.value = AnalysisState.AnalyzingPhoto

            // Try Gemini first if API key is set and device is online
            val isGeminiAvailable = geminiService.isAvailable()
            val isDeviceOnline = isOnline()
            Log.d(TAG, "Gemini available: $isGeminiAvailable, Online: $isDeviceOnline")

            // Track whether we're falling back from a Gemini failure
            var wasFallback = false

            if (isGeminiAvailable && isDeviceOnline) {
                Log.d(TAG, "Attempting Gemini analysis...")
                val geminiResult = tryGeminiAnalysis(photoUri)
                if (geminiResult != null) {
                    Log.d(TAG, "Gemini succeeded with ${geminiResult.results.size} results")
                    _analysisState.value = AnalysisState.Results(geminiResult)
                    saveResults(geminiResult, photoUri.toString())
                    return@launch
                }
                Log.d(TAG, "Gemini returned null, falling back to TFLite")
                wasFallback = true
            }

            // Use TFLite (either as primary or as fallback from Gemini)
            runTFLiteAnalysis(photoUri, wasFallback)
        }
    }

    /**
     * Try to analyze the photo using Gemini 2.0 Flash.
     * Returns a PhotoAnalysisSession on success, or null if anything fails.
     */
    private suspend fun tryGeminiAnalysis(photoUri: Uri): PhotoAnalysisSession? {
        return try {
            Log.d(TAG, "Loading bitmap from URI: $photoUri")
            // Load the photo as a Bitmap for Gemini (it accepts Bitmap directly)
            val bitmap = withContext(Dispatchers.IO) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    // Android 9+ — use ImageDecoder (more efficient, handles EXIF rotation)
                    val source = ImageDecoder.createSource(application.contentResolver, photoUri)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        // Force software rendering to get a mutable ARGB bitmap
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    }
                } else {
                    // Older Android — use BitmapFactory via MediaStore
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(application.contentResolver, photoUri)
                }
            }

            Log.d(TAG, "Bitmap loaded: ${bitmap.width}x${bitmap.height}")

            // Call Gemini with the bitmap and body area
            val result = geminiService.analyze(bitmap, _selectedBodyArea.value)
            if (result == null) {
                Log.w(TAG, "GeminiService.analyze() returned null")
                return null
            }

            // Only use Gemini results if it found actual conditions
            if (result.conditions.isEmpty()) return null

            PhotoAnalysisSession(
                photoPath = photoUri.toString(),
                bodyArea = _selectedBodyArea.value,
                results = result.conditions,
                isSuccessful = true,
                aiSummary = result.summary,
                isOnlineAnalysis = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Gemini analysis error: ${e.message}", e)
            null
        }
    }

    /**
     * Run the TFLite model on the selected photo (existing on-device analysis).
     * Updates analysisState with the results when done.
     */
    private suspend fun runTFLiteAnalysis(photoUri: Uri, wasFallback: Boolean = false) {
        when (val result = mlManager.analyze(photoUri)) {
            is MLManager.InferenceResult.Success -> {
                val session = PhotoAnalysisSession(
                    photoPath = photoUri.toString(),
                    bodyArea = _selectedBodyArea.value,
                    results = result.results,
                    isSuccessful = true,
                    isOnlineAnalysis = false,
                    fallbackMessage = if (wasFallback) "Online analysis unavailable. Using offline model." else ""
                )
                _isDemoMode.value = result.isDemoMode
                _analysisState.value = AnalysisState.Results(session)

                // Auto-save result to database
                saveResults(session, photoUri.toString())
            }
            is MLManager.InferenceResult.NoMatch -> {
                val session = PhotoAnalysisSession(
                    photoPath = photoUri.toString(),
                    bodyArea = _selectedBodyArea.value,
                    results = emptyList(),
                    isSuccessful = false,
                    errorMessage = result.message,
                    isOnlineAnalysis = false
                )
                _analysisState.value = AnalysisState.Results(session)
            }
            is MLManager.InferenceResult.Error -> {
                _analysisState.value = AnalysisState.Error(result.message)
            }
        }
    }

    /**
     * Save the analysis results to the Room database.
     * Linked to the currently active profile.
     */
    private suspend fun saveResults(session: PhotoAnalysisSession, photoPath: String) {
        val activeProfile = profileRepository.activeProfile.first() ?: return
        if (session.results.isEmpty()) return

        val topResult = session.results.first()
        // Serialize the results list to JSON for database storage
        val resultsJson = session.results.joinToString(",", "[", "]") { result ->
            """{"conditionId":"${result.conditionId}","name":"${result.displayName}","confidence":${result.confidence},"category":"${result.category}"}"""
        }

        val analysisResult = AnalysisResult(
            profileId = activeProfile.id,
            bodyArea = session.bodyArea,
            photoPath = photoPath,
            topResultsJson = resultsJson,
            topConditionId = topResult.conditionId,
            topConfidence = topResult.confidence
        )

        val savedId = analysisRepository.savePhotoAnalysis(analysisResult)
        _savedResultId.value = savedId
    }

    fun resetState() {
        _analysisState.value = AnalysisState.Idle
        _selectedPhotoUri.value = null
        _savedResultId.value = null
        _hasNavigatedToResults.value = false
    }

    override fun onCleared() {
        super.onCleared()
        // Do NOT call mlManager.close() here — MLManager is a Singleton that
        // outlives any single ViewModel. Closing it would break future analysis
        // attempts when a new ViewModel is created.
    }
}
