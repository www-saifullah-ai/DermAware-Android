package ai.saifullah.dermaware.ui.screen.analyze

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import java.io.File

/**
 * Analyze screen — where users select or take a photo and run ML analysis.
 * Handles camera permission, photo selection, body area selection, and analysis flow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResults: () -> Unit,
    viewModel: AnalyzeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val analysisState by viewModel.analysisState.collectAsState()
    val selectedBodyArea by viewModel.selectedBodyArea.collectAsState()
    val selectedPhotoUri by viewModel.selectedPhotoUri.collectAsState()
    val isDemoMode by viewModel.isDemoMode.collectAsState()
    val hasNavigatedToResults by viewModel.hasNavigatedToResults.collectAsState()

    // Navigate to results screen once analysis completes.
    // The hasNavigatedToResults flag prevents re-navigation when the user presses back.
    LaunchedEffect(analysisState) {
        if (analysisState is AnalyzeViewModel.AnalysisState.Results && !hasNavigatedToResults) {
            viewModel.markResultsNavigated()
            onNavigateToResults()
        }
    }

    // Temporary URI for camera-captured photo
    var cameraPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Whether to show the "camera permission denied" dialog
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }

    // Whether to launch the camera after permission is granted
    var pendingCameraLaunch by remember { mutableStateOf(false) }

    // Camera photo capture launcher (declared before permission launcher so it can be referenced)
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraPhotoUri?.let { uri ->
                viewModel.setPhotoUri(uri)
            }
        }
    }

    // Gallery photo picker launcher
    val pickFromGalleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.setPhotoUri(it) }
    }

    // Camera permission launcher — sets flag to launch camera on grant, shows dialog on deny
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted — flag to launch camera
            pendingCameraLaunch = true
        } else {
            // Permission denied — show explanation dialog
            showPermissionDeniedDialog = true
        }
    }

    // Launch camera after permission is granted (uses LaunchedEffect to avoid referencing order issues)
    LaunchedEffect(pendingCameraLaunch) {
        if (pendingCameraLaunch) {
            pendingCameraLaunch = false
            val photoFile = File(context.externalCacheDir, "dermaware_capture_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
            cameraPhotoUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    // Create a temporary file URI for the camera to save to
    fun createCameraUri(): Uri {
        val photoFile = File(context.externalCacheDir, "dermaware_capture_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
    }

    fun launchCamera() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasCameraPermission) {
            val uri = createCameraUri()
            cameraPhotoUri = uri
            takePictureLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Camera permission denied dialog — explains why camera is needed and offers gallery
    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            icon = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
            title = { Text("Camera Permission Required") },
            text = {
                Text("DermAware needs camera access to take photos of skin areas for analysis. " +
                    "You can grant permission in your device settings, or use the Gallery button to select an existing photo instead.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDeniedDialog = false
                    // Open gallery as an alternative
                    pickFromGalleryLauncher.launch("image/*")
                }) {
                    Text("Use Gallery")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDeniedDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text("Analyze Skin Photo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Loading overlay when analyzing
        if (analysisState is AnalyzeViewModel.AnalysisState.AnalyzingPhoto) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(56.dp))
                    Text(
                        text = "Analyzing your photo...",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "This may take a few seconds",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Demo mode banner
            if (isDemoMode) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(16.dp))
                        Text(
                            text = "Demo mode: Add dermaware_model.tflite to assets/ml/ for real analysis",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            // Step 1: Select body area
            Text(
                text = "Step 1: Select body area",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            val bodyAreas = listOf("Face", "Scalp", "Neck", "Arm", "Hand", "Trunk", "Back", "Leg", "Foot")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bodyAreas) { area ->
                    FilterChip(
                        selected = selectedBodyArea.equals(area, ignoreCase = true),
                        onClick = { viewModel.setBodyArea(area.lowercase()) },
                        label = { Text(area) }
                    )
                }
            }

            // Step 2: Take or select photo
            Text(
                text = "Step 2: Take or select a photo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // Photo tips
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("📸 Photo tips for best results:", style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold)
                    Text("• Good lighting — natural light or bright indoor light", style = MaterialTheme.typography.bodySmall)
                    Text("• 15-20cm distance from skin", style = MaterialTheme.typography.bodySmall)
                    Text("• Keep camera steady — avoid blur", style = MaterialTheme.typography.bodySmall)
                    Text("• Fill the frame with the affected area", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Photo preview or placeholder
            if (selectedPhotoUri != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    AsyncImage(
                        model = selectedPhotoUri,
                        contentDescription = "Selected photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Change photo button overlay
                    IconButton(
                        onClick = { viewModel.setPhotoUri(Uri.EMPTY) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove photo",
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            } else {
                // Photo picker placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(16.dp)
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "No photo selected",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Camera and gallery buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { launchCamera() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null,
                        modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Camera")
                }
                OutlinedButton(
                    onClick = { pickFromGalleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null,
                        modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Gallery")
                }
            }

            // Error message with retry button if analysis failed
            if (analysisState is AnalyzeViewModel.AnalysisState.Error) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = (analysisState as AnalyzeViewModel.AnalysisState.Error).message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = { viewModel.analyzePhoto() },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null,
                                modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Retry")
                        }
                    }
                }
            }

            // Step 3: Analyze button
            val canAnalyze = selectedPhotoUri != null &&
                    selectedPhotoUri != Uri.EMPTY &&
                    analysisState !is AnalyzeViewModel.AnalysisState.AnalyzingPhoto

            Button(
                onClick = { viewModel.analyzePhoto() },
                modifier = Modifier.fillMaxWidth(),
                enabled = canAnalyze,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null,
                    modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Analyze Photo",
                    modifier = Modifier.padding(vertical = 4.dp),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            // Disclaimer at the bottom
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Info, contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(14.dp).padding(top = 2.dp))
                    Text(
                        text = "For educational awareness only — NOT a medical diagnosis. Always consult a qualified dermatologist.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
