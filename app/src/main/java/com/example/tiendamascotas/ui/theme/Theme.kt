package com.example.tiendamascotas.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define color schemes for dark and light themes
private val DarkColorScheme = darkColorScheme(
    primary = DarkGreen,        // Color primario oscuro
    secondary = DarkBlue,       // Color secundario oscuro
    tertiary = MediumGreen      // Color terciario medio
)

private val LightColorScheme = lightColorScheme(
    primary = LightGreen,       // Color primario claro
    secondary = LightBlue,      // Color secundario claro
    tertiary = MediumBlue       // Color terciario medio
)

@Composable
fun TiendaMascotasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Enable dynamic color support
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Use dynamic color scheme if available (Android 12+)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Adjust the system UI (status bar color)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Apply the selected color scheme and typography to the app's theme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
