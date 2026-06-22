package com.example.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val CosmicGoldColorScheme = darkColorScheme(
    primary = MetallicGold,
    secondary = EmeraldGlow,
    tertiary = BrightGold,
    background = SlateBg,
    surface = CardSlateBg,
    onPrimary = SlateBg,
    onSecondary = SlateBg,
    onBackground = TextSilver,
    onSurface = TextSilver,
    error = DangerRed
)

private val LightGoldColorScheme = lightColorScheme(
    primary = MetallicGold,
    secondary = EmeraldGlow,
    tertiary = DarkGold,
    background = SlateBg, // Maintain gorgeous dark slate mode as the default for luxurious aesthetics
    surface = CardSlateBg,
    onPrimary = SlateBg,
    onSecondary = SlateBg,
    onBackground = TextSilver,
    onSurface = TextSilver,
    error = DangerRed
)

private val NeonLightColorScheme = darkColorScheme(
    primary = Color(0xFF00FFCC),
    secondary = Color(0xFF00FFFF),
    tertiary = Color(0xFF00E676),
    background = Color(0xFF0C1014),
    surface = Color(0xFF162231),
    onPrimary = Color(0xFF0C1014),
    onSecondary = Color(0xFF0C1014),
    onBackground = Color(0xFFE0F7FA),
    onSurface = Color(0xFFE0F7FA),
    error = DangerRed
)

private val AcademicQuietColorScheme = darkColorScheme(
    primary = Color(0xFF6366F1),
    secondary = Color(0xFF8B5CF6),
    tertiary = Color(0xFF3B82F6),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    error = DangerRed
)

private val SpaceNebulaColorScheme = darkColorScheme(
    primary = Color(0xFFA855F7),
    secondary = Color(0xFFEC4899),
    tertiary = Color(0xFFF43F5E),
    background = Color(0xFF090514),
    surface = Color(0xFF180F30),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFF2EAFE),
    onSurface = Color(0xFFF2EAFE),
    error = DangerRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable Android dynamic colors to guarantee our gorgeous premium custom gold branding is applied on all devices
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("SmartPrefs", Context.MODE_PRIVATE)
    val chosenTheme = sharedPrefs.getString("chosen_theme", "golden_dark") ?: "golden_dark"

    val colorScheme = when (chosenTheme) {
        "neon_light" -> NeonLightColorScheme
        "academic_quiet" -> AcademicQuietColorScheme
        "space_nebula" -> SpaceNebulaColorScheme
        else -> if (darkTheme) CosmicGoldColorScheme else LightGoldColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
