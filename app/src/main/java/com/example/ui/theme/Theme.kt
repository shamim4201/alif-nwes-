package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = NewsNavyDark,
    primaryContainer = NewsNavySecondary,
    onPrimaryContainer = Color.White,
    secondary = NewsGoldAccent,
    onSecondary = Color.Black,
    error = NewsBreakingRed,
    background = NewsBackgroundDark,
    surface = NewsSurfaceDark,
    surfaceVariant = NewsSurfaceVariantDark,
    onBackground = NewsTextPrimaryDark,
    onSurface = NewsTextPrimaryDark,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = NewsNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = NewsNavySecondary,
    onPrimaryContainer = Color.White,
    secondary = NewsGoldAccent,
    onSecondary = Color.White,
    error = NewsBreakingRed,
    background = NewsBackgroundLight,
    surface = NewsSurfaceLight,
    surfaceVariant = NewsSurfaceVariantLight,
    onBackground = NewsTextPrimaryLight,
    onSurface = NewsTextPrimaryLight,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
