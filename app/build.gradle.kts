plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // KSP for Room database and Hilt annotation processing
    alias(libs.plugins.ksp)
    // Hilt for dependency injection
    alias(libs.plugins.hilt.android)
}

// Read the OpenRouter API key from local.properties (not checked into version control)
val openRouterApiKey: String = providers.gradleProperty("OPENROUTER_API_KEY").getOrElse(
    rootProject.file("local.properties").let { file ->
        if (file.exists()) {
            file.readLines().firstOrNull { it.startsWith("OPENROUTER_API_KEY=") }
                ?.substringAfter("=")?.trim() ?: ""
        } else ""
    }
)

// Read release keystore config from local.properties (not checked into version control)
val keystorePath: String = providers.gradleProperty("RELEASE_KEYSTORE_PATH").getOrElse(
    rootProject.file("local.properties").let { file ->
        if (file.exists()) {
            file.readLines().firstOrNull { it.startsWith("RELEASE_KEYSTORE_PATH=") }
                ?.substringAfter("=")?.trim() ?: ""
        } else ""
    }
)
val keystorePassword: String = providers.gradleProperty("RELEASE_KEYSTORE_PASSWORD").getOrElse(
    rootProject.file("local.properties").let { file ->
        if (file.exists()) {
            file.readLines().firstOrNull { it.startsWith("RELEASE_KEYSTORE_PASSWORD=") }
                ?.substringAfter("=")?.trim() ?: ""
        } else ""
    }
)
val releaseKeyAlias: String = providers.gradleProperty("RELEASE_KEY_ALIAS").getOrElse(
    rootProject.file("local.properties").let { file ->
        if (file.exists()) {
            file.readLines().firstOrNull { it.startsWith("RELEASE_KEY_ALIAS=") }
                ?.substringAfter("=")?.trim() ?: ""
        } else ""
    }
)
val releaseKeyPassword: String = providers.gradleProperty("RELEASE_KEY_PASSWORD").getOrElse(
    rootProject.file("local.properties").let { file ->
        if (file.exists()) {
            file.readLines().firstOrNull { it.startsWith("RELEASE_KEY_PASSWORD=") }
                ?.substringAfter("=")?.trim() ?: ""
        } else ""
    }
)

android {
    namespace = "ai.saifullah.dermaware"
    compileSdk = 36

    // Release signing config — reads keystore info from local.properties
    signingConfigs {
        if (keystorePath.isNotEmpty()) {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = keystorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    defaultConfig {
        applicationId = "ai.saifullah.dermaware"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Make the OpenRouter API key available in Kotlin code via BuildConfig.OPENROUTER_API_KEY
        buildConfigField("String", "OPENROUTER_API_KEY", "\"$openRouterApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Use release signing config if keystore is configured in local.properties
            if (keystorePath.isNotEmpty()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Allow assets in the assets/ folder (needed for TFLite model and labels)
    androidResources {
        noCompress += "tflite"
    }

    // Room schema export directory for database migrations
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // Compose BOM — manages all Compose library versions
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Extended Material Icons (for medical/health icons)
    implementation(libs.androidx.compose.material.icons.extended)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Room database — local offline storage
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt dependency injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // CameraX — for photo capture
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Coil — for loading images from disk/URI
    implementation(libs.coil.compose)

    // TensorFlow Lite — on-device ML inference (core interpreter only)
    implementation(libs.tflite)

    // Coroutines — for async operations
    implementation(libs.kotlinx.coroutines.android)

    // DataStore — for storing user preferences (language, dark mode, etc.)
    implementation(libs.androidx.datastore.preferences)

    // WorkManager — for scheduling monthly skin check reminders
    implementation(libs.androidx.work.runtime.ktx)

    // ExifInterface — for reading camera photo rotation metadata
    implementation(libs.androidx.exifinterface)

    // Splash screen — shows app icon on launch
    implementation(libs.androidx.core.splashscreen)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
