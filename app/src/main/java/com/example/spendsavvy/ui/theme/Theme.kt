package com.example.spendsavvy.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF423E3E),
    secondary = Color(0xFF423E3E),
    tertiary = Color(0xFF423E3E),
    background = Color.Black ,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF423E3E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF625D5D),
    onPrimaryContainer = Color(0xFFD9D9D9),
    inversePrimary = Color(0xFFB0B0B0),
    secondary = Color(0xFF423E3E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF625D5D),
    onSecondaryContainer = Color(0xFFD9D9D9),
    tertiary = Color(0xFF423E3E),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF625D5D),
    onTertiaryContainer = Color(0xFFD9D9D9),
    background = Color.White,
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceTint = Color(0xFF423E3E),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFE6E1E5),
    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFF2B8B5),
    onErrorContainer = Color(0xFF601410),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color.Black,
    surfaceBright = Color.White,
    surfaceContainer = Color(0xFFEEEEEE),
    surfaceContainerHigh = Color(0xFFE0E0E0),
    surfaceContainerHighest = Color(0xFFD6D6D6),
    surfaceContainerLow = Color(0xFFF6F6F6),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFFAFAFA)
)

@Composable
fun SpendSavvyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}