package ai.saifullah.dermaware.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// DermAware light color scheme — clean medical teal aesthetic
private val DermAwareLightColorScheme = lightColorScheme(
    primary = DermAwareTeal40,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = DermAwareTeal80.copy(alpha = 0.3f),
    onPrimaryContainer = DermAwareTeal40,
    secondary = DermAwareGreen40,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = DermAwareGreen80.copy(alpha = 0.3f),
    onSecondaryContainer = DermAwareGreen40,
    tertiary = DermAwareBlue40,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = DermAwareBlue80.copy(alpha = 0.3f),
    onTertiaryContainer = DermAwareBlue40,
    error = DermAwareError40,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = DermAwareError40.copy(alpha = 0.15f),
    onErrorContainer = DermAwareError40,
    background = DermAwareSurface,
    onBackground = androidx.compose.ui.graphics.Color(0xFF0D1F22),
    surface = DermAwareSurface,
    onSurface = androidx.compose.ui.graphics.Color(0xFF0D1F22),
    surfaceVariant = DermAwareSurfaceVariant,
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF3D5A5F),
    outline = DermAwareTeal40.copy(alpha = 0.4f),
    outlineVariant = DermAwareTeal80.copy(alpha = 0.3f)
)

// DermAware dark color scheme
private val DermAwareDarkColorScheme = darkColorScheme(
    primary = DermAwareTeal80,
    onPrimary = androidx.compose.ui.graphics.Color(0xFF003640),
    primaryContainer = DermAwareTeal40.copy(alpha = 0.3f),
    onPrimaryContainer = DermAwareTeal80,
    secondary = DermAwareGreen80,
    onSecondary = androidx.compose.ui.graphics.Color(0xFF003910),
    secondaryContainer = DermAwareGreen40.copy(alpha = 0.3f),
    onSecondaryContainer = DermAwareGreen80,
    tertiary = DermAwareBlue80,
    onTertiary = androidx.compose.ui.graphics.Color(0xFF003258),
    error = DermAwareError80,
    onError = androidx.compose.ui.graphics.Color(0xFF680010),
    errorContainer = DermAwareError40.copy(alpha = 0.3f),
    onErrorContainer = DermAwareError80,
    background = DermAwareDarkBackground,
    onBackground = androidx.compose.ui.graphics.Color(0xFFE0F2F5),
    surface = DermAwareDarkSurface,
    onSurface = androidx.compose.ui.graphics.Color(0xFFE0F2F5),
    surfaceVariant = DermAwareDarkSurfaceVariant,
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF99C5CB),
    outline = DermAwareTeal80.copy(alpha = 0.4f),
    outlineVariant = DermAwareTeal40.copy(alpha = 0.3f)
)

/**
 * DermAware theme composable.
 * Applies the teal medical color palette and typography to the entire app.
 *
 * Dynamic color (Android 12+) is DISABLED — we want consistent medical branding
 * regardless of the user's wallpaper color.
 */
@Composable
fun DermAwareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DermAwareDarkColorScheme else DermAwareLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Make status bar use our background color
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
