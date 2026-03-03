package ai.saifullah.dermaware

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * DermAware Application class.
 * The @HiltAndroidApp annotation triggers Hilt's code generation and
 * sets up the dependency injection container for the entire app.
 * This must be registered in AndroidManifest.xml as android:name=".DermAwareApp"
 */
@HiltAndroidApp
class DermAwareApp : Application()
