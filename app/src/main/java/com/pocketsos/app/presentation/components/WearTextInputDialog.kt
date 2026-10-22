package com.pocketsos.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Dialog
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.theme.SurfaceVariantDark

@Composable
fun WearTextInputDialog(
    showDialog: Boolean,
    title: String,
    initialText: String = "",
    presets: List<String> = listOf("Lentes", "Billetera", "Medicinas", "Botella", "Paraguas", "Dinero", "Reloj", "Carnet"),
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var textValue by remember { mutableStateOf(initialText) }
    val listState = rememberScalingLazyListState()

    LaunchedEffect(showDialog, initialText) {
        if (showDialog) {
            textValue = initialText
        }
    }

    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismiss
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = title,
                    style = MaterialTheme.typography.title3.copy(fontWeight = FontWeight.Bold),
                    color = GreenPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                )
            }

            // Current text display / preview
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDark)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (textValue.isEmpty()) {
                        Text(
                            text = "Toca un sugerido o escribe...",
                            style = MaterialTheme.typography.caption1.copy(
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = textValue,
                                style = MaterialTheme.typography.body1.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Borrar",
                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        if (textValue.isNotEmpty()) {
                                            textValue = textValue.dropLast(1)
                                        }
                                    }
                            )
                        }
                    }
                }
            }

            // Confirm / Cancel buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = SurfaceVariantDark,
                            contentColor = MaterialTheme.colors.onSurface
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cancelar",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (textValue.isNotBlank()) {
                                onConfirm(textValue.trim())
                                onDismiss()
                            }
                        },
                        enabled = textValue.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = GreenPrimary,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Aceptar",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Quick preset chips
            item {
                Text(
                    text = "Sugerencias rápidas:",
                    style = MaterialTheme.typography.caption2.copy(
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }

            items(presets.size) { index ->
                val preset = presets[index]
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 2.dp),
                    onClick = {
                        textValue = preset
                    },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceVariantDark,
                        contentColor = MaterialTheme.colors.onSurface
                    ),
                    label = {
                        Text(
                            text = preset,
                            style = MaterialTheme.typography.body2,
                            maxLines = 1
                        )
                    }
                )
            }
        }
    }
}
