package com.pocketsos.app.presentation.screens.reminders

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.pocketsos.app.presentation.components.PocketScaffold
import com.pocketsos.app.presentation.components.WearTextInputDialog
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.theme.SurfaceVariantDark
import com.pocketsos.app.presentation.viewmodel.PocketSosViewModel

@Composable
fun RemindersScreen(
    viewModel: PocketSosViewModel,
    onNavigateBack: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val reminders by viewModel.reminders.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    PocketScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(top = 24.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.size(32.dp),
                        onClick = onNavigateBack,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = SurfaceVariantDark,
                            contentColor = MaterialTheme.colors.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Recordatorios",
                        style = MaterialTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFFFB74D)
                    )
                }
            }

            // Add reminder button
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 4.dp),
                    onClick = { showAddDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = Color(0xFFFFB74D).copy(alpha = 0.2f),
                        contentColor = Color(0xFFFFB74D)
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Nuevo recordatorio",
                            tint = Color(0xFFFFB74D),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "+ Recordatorio",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                )
            }

            // Empty state
            if (reminders.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Alarm,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No tienes recordatorios pendientes",
                            style = MaterialTheme.typography.caption1,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Reminders list
            items(reminders, key = { it.id }) { reminder ->
                val backgroundColor by animateColorAsState(
                    targetValue = if (reminder.isCompleted) GreenPrimary.copy(alpha = 0.15f) else SurfaceDark,
                    label = "rem_bg"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Chip(
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.toggleReminder(reminder.id) },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = backgroundColor,
                            contentColor = MaterialTheme.colors.onBackground
                        ),
                        icon = {
                            if (reminder.isCompleted) {
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
                                text = reminder.title,
                                style = MaterialTheme.typography.body1.copy(
                                    fontSize = 13.sp,
                                    textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        secondaryLabel = reminder.timeTag?.let { tag ->
                            {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.caption2,
                                    color = Color(0xFFFFB74D).copy(alpha = 0.8f)
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Button(
                        modifier = Modifier.size(32.dp),
                        onClick = { viewModel.deleteReminder(reminder.id) },
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

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    WearTextInputDialog(
        showDialog = showAddDialog,
        title = "Nuevo recordatorio",
        presets = listOf("Apagar estufa", "Cerrar llaves agua", "Sacar la basura", "Regar plantas", "Cerrar ventanas", "Alimentar mascota", "Desconectar plancha"),
        onConfirm = { title ->
            viewModel.addReminder(title, timeTag = "Antes de salir")
        },
        onDismiss = { showAddDialog = false }
    )
}
