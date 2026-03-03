// Top-level build file — applied to all sub-projects/modules
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // KSP plugin for annotation processing (used by Room and Hilt)
    alias(libs.plugins.ksp) apply false
    // Hilt dependency injection plugin
    alias(libs.plugins.hilt.android) apply false
}
