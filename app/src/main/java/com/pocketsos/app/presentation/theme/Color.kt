package com.pocketsos.app.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val GreenPrimary = Color(0xFF00E676)
val GreenSecondary = Color(0xFF69F0AE)
val GreenBackgroundGlow = Color(0x2200E676)

val BluePrimary = Color(0xFF00B0FF)
val BlueSecondary = Color(0xFF80D8FF)
val BlueCard = Color(0xFF131D2A)

val DarkBackground = Color(0xFF0D0F12)
val SurfaceDark = Color(0xFF181C24)
val SurfaceVariantDark = Color(0xFF222834)
val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val AccentRed = Color(0xFFFF5252)
val AccentAmber = Color(0xFFFFAB00)

val PocketSosWearColors = Colors(
    primary = GreenPrimary,
    primaryVariant = GreenSecondary,
    secondary = BluePrimary,
    secondaryVariant = BlueSecondary,
    background = DarkBackground,
    surface = SurfaceDark,
    error = AccentRed,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onError = Color.White
)
