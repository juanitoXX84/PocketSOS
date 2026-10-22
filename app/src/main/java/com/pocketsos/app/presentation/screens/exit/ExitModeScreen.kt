package com.pocketsos.app.presentation.screens.exit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.pocketsos.app.presentation.components.ChecklistChip
import com.pocketsos.app.presentation.components.ConfirmationDialog
import com.pocketsos.app.presentation.components.PocketScaffold
import com.pocketsos.app.presentation.components.ProgressSummaryCard
import com.pocketsos.app.presentation.components.WearTextInputDialog
import com.pocketsos.app.presentation.theme.GreenBackgroundGlow
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.theme.SurfaceVariantDark
import com.pocketsos.app.presentation.viewmodel.PocketSosViewModel

@Composable
fun ExitModeScreen(
    viewModel: PocketSosViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val items by viewModel.exitModeItems.collectAsState()
    val isReady by viewModel.isExitModeReady.collectAsState()
    val completedCount = items.count { it.isChecked }
    val totalCount = items.size

    var showAddDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showFinishDialog by remember { mutableStateOf(false) }
    var showResetDefaultsDialog by remember { mutableStateOf(false) }

    PocketScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with Back button and Title
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
                        text = "Modo salida",
                        style = MaterialTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
                        color = GreenPrimary
                    )
                }
            }

            // Progress Summary Card
            item {
                ProgressSummaryCard(
                    completedCount = completedCount,
                    totalCount = totalCount,
                    title = "Objetos de salida",
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 4.dp)
                )
            }

            // Banner when ALL items are checked
            if (isReady && totalCount > 0) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(GreenBackgroundGlow)
                            .border(1.dp, GreenPrimary, RoundedCornerShape(14.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "¡Todo listo para salir! 🎉",
                                style = MaterialTheme.typography.title3.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "No olvides cerrar la puerta con llave.",
                                style = MaterialTheme.typography.caption2,
                                color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Button to Finish Exit & log to History
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 4.dp),
                    onClick = { showFinishDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = if (isReady) GreenPrimary else SurfaceDark,
                        contentColor = if (isReady) Color.Black else MaterialTheme.colors.onBackground
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Finalizar salida",
                            tint = if (isReady) Color.Black else GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Finalizar salida",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    secondaryLabel = {
                        Text(
                            text = "Guardar en historial y reiniciar",
                            style = MaterialTheme.typography.caption2,
                            color = if (isReady) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                        )
                    }
                )
            }

            // Checklist Items
            items(items, key = { it.id }) { item ->
                ChecklistChip(
                    title = item.title,
                    isChecked = item.isChecked,
                    onToggle = { viewModel.toggleExitItem(item.id) },
                    onDelete = { viewModel.deleteExitItem(item.id) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 2.dp)
                )
            }

            // Action: Add custom item
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(top = 6.dp, bottom = 3.dp),
                    onClick = { showAddDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceDark,
                        contentColor = GreenPrimary
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar objeto",
                            tint = GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Agregar objeto",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                )
            }

            // Action: New Exit / Reset checklist
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 3.dp),
                    onClick = { showResetDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceDark,
                        contentColor = MaterialTheme.colors.onBackground
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Nueva salida",
                            tint = Color(0xFF80D8FF),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Nueva salida (desmarcar)",
                            style = MaterialTheme.typography.body2
                        )
                    }
                )
            }

            // Action: Restore Default Items
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 3.dp),
                    onClick = { showResetDefaultsDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceVariantDark,
                        contentColor = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Restore,
                            contentDescription = "Restablecer predeterminados",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Restablecer originales",
                            style = MaterialTheme.typography.caption1
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    // Dialog: Add Custom Item
    WearTextInputDialog(
        showDialog = showAddDialog,
        title = "Agregar objeto",
        presets = listOf("Lentes", "Billetera", "Medicinas", "Botella", "Paraguas", "Dinero", "Reloj", "Carnet"),
        onConfirm = { newTitle ->
            viewModel.addExitItem(newTitle)
        },
        onDismiss = { showAddDialog = false }
    )

    // Dialog: Confirm Reset Checklist
    ConfirmationDialog(
        showDialog = showResetDialog,
        title = "¿Iniciar nueva salida?",
        message = "Se desmarcarán todos los objetos de la lista.",
        confirmButtonColor = GreenPrimary,
        onConfirm = {
            viewModel.resetExitMode()
        },
        onDismiss = { showResetDialog = false }
    )

    // Dialog: Confirm Finish Exit
    ConfirmationDialog(
        showDialog = showFinishDialog,
        title = "¿Finalizar salida?",
        message = "Se guardará el registro con $completedCount/$totalCount objetos en el historial.",
        confirmButtonColor = GreenPrimary,
        onConfirm = {
            viewModel.finishExitSession("Modo salida")
            onNavigateToHistory()
        },
        onDismiss = { showFinishDialog = false }
    )

    // Dialog: Restore Default items
    ConfirmationDialog(
        showDialog = showResetDefaultsDialog,
        title = "¿Restablecer lista?",
        message = "Volver a los 6 objetos originales (Llaves, Cartera, etc.).",
        confirmButtonColor = Color(0xFFFF5252),
        onConfirm = {
            viewModel.resetExitModeToDefaults()
        },
        onDismiss = { showResetDefaultsDialog = false }
    )
}
