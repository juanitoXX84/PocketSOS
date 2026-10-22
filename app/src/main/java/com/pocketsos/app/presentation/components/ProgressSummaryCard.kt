package com.pocketsos.app.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.pocketsos.app.presentation.theme.BluePrimary
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.theme.SurfaceVariantDark

@Composable
fun ProgressSummaryCard(
    completedCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier,
    title: String = "Progreso"
) {
    val progress = if (totalCount == 0) 0f else (completedCount.toFloat() / totalCount).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress_anim")
    val isComplete = totalCount > 0 && completedCount == totalCount

    val accentColor by animateColorAsState(
        targetValue = if (isComplete) GreenPrimary else BluePrimary,
        label = "accent_color"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = if (isComplete) 0.6f else 0.25f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 10.dp, horizontal = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(38.dp)
            ) {
                CircularProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.size(38.dp),
                    indicatorColor = accentColor,
                    trackColor = SurfaceVariantDark,
                    strokeWidth = 3.5.dp
                )
                if (isComplete) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Listo",
                        tint = GreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    val percent = (progress * 100).toInt()
                    Text(
                        text = "$percent%",
                        style = MaterialTheme.typography.caption2.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = MaterialTheme.colors.onBackground
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isComplete) "¡Todo listo! 🚀" else title,
                    style = MaterialTheme.typography.title3.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (isComplete) GreenPrimary else MaterialTheme.colors.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$completedCount de $totalCount objetos listos",
                    style = MaterialTheme.typography.caption1.copy(
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}
