# DermAware ProGuard Rules

# Keep line number info for crash stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ── Room Database ────────────────────────────────────────────────────────────
# Keep Room entity, DAO, and database classes so Room can find them at runtime
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# ── Hilt / Dagger ────────────────────────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.hilt.**

# ── TensorFlow Lite ──────────────────────────────────────────────────────────
# Keep TFLite classes — needed for model loading and inference
-keep class org.tensorflow.** { *; }
-dontwarn org.tensorflow.**

# ── Kotlin Coroutines ────────────────────────────────────────────────────────
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ── Coil image loading ────────────────────────────────────────────────────────
-dontwarn coil.**

# ── DataStore ────────────────────────────────────────────────────────────────
-keep class androidx.datastore.** { *; }

# ── Keep DermAware app data model classes (used with JSON serialization) ──────
-keep class ai.saifullah.dermaware.data.model.** { *; }
-keep class ai.saifullah.dermaware.data.database.entity.** { *; }
