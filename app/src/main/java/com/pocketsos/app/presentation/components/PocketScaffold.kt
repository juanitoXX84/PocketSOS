package com.pocketsos.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults

@Composable
fun PocketScaffold(
    listState: ScalingLazyListState? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        timeText = {
            TimeText(
                timeTextStyle = TimeTextDefaults.timeTextStyle(
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                )
            )
        },
        positionIndicator = {
            if (listState != null) {
                PositionIndicator(scalingLazyListState = listState)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
