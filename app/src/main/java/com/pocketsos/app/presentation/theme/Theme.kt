package com.pocketsos.app.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun PocketSosTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = PocketSosWearColors,
        typography = PocketSosTypography,
        content = content
    )
}
