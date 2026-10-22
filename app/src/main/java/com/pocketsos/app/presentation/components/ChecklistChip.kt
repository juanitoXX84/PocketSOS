package com.pocketsos.app.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.theme.SurfaceVariantDark

@Composable
fun ChecklistChip(
    title: String,
    isChecked: Boolean,
    onToggle: () -> Unit,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isChecked) GreenPrimary.copy(alpha = 0.15f) else SurfaceDark,
        label = "chip_bg"
    )

    val textColor by animateColorAsState(
        targetValue = if (isChecked) MaterialTheme.colors.onBackground.copy(alpha = 0.5f) else MaterialTheme.colors.onBackground,
        label = "chip_text"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Chip(
            modifier = Modifier.weight(1f),
            onClick = onToggle,
            colors = ChipDefaults.chipColors(
                backgroundColor = backgroundColor,
                contentColor = textColor
            ),
            icon = {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Completado",
                        tint = GreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "Pendiente",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            label = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 13.sp,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )

        if (onDelete != null) {
            Spacer(modifier = Modifier.width(4.dp))
            Button(
                modifier = Modifier.size(32.dp),
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SurfaceVariantDark,
                    contentColor = Color(0xFFFF6B6B)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Eliminar",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
