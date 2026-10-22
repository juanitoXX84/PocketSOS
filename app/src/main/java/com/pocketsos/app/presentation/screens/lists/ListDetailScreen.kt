package com.pocketsos.app.presentation.screens.lists

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import com.pocketsos.app.presentation.theme.BluePrimary
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.theme.SurfaceVariantDark
import com.pocketsos.app.presentation.viewmodel.PocketSosViewModel

@Composable
fun ListDetailScreen(
    listId: String,
    viewModel: PocketSosViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToExitMode: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val customLists by viewModel.customLists.collectAsState()
    val currentList = customLists.find { it.id == listId }

    var showAddItemDialog by remember { mutableStateOf(false) }
    var showEditTitleDialog by remember { mutableStateOf(false) }
    var showDeleteListDialog by remember { mutableStateOf(false) }
    var showResetListDialog by remember { mutableStateOf(false) }

    if (currentList == null) {
        PocketScaffold {
            Text(
                text = "Lista no encontrada",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

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
                        text = currentList.title,
                        style = MaterialTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
                        color = BluePrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        modifier = Modifier.size(28.dp),
                        onClick = { showEditTitleDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = SurfaceVariantDark,
                            contentColor = MaterialTheme.colors.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar nombre",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Progress Summary Card
            item {
                ProgressSummaryCard(
                    completedCount = currentList.completedCount,
                    totalCount = currentList.totalCount,
                    title = currentList.title,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 4.dp)
                )
            }

            // Action: Use in Exit Mode
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 3.dp),
                    onClick = {
                        viewModel.loadCustomListIntoExitMode(currentList.id)
                        onNavigateToExitMode()
                    },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = GreenPrimary.copy(alpha = 0.2f),
                        contentColor = GreenPrimary
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Usar en Modo salida",
                            tint = GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Usar en Modo salida",
                            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                )
            }

            // Checklist Items
            items(currentList.items, key = { it.id }) { item ->
                ChecklistChip(
                    title = item.title,
                    isChecked = item.isChecked,
                    onToggle = { viewModel.toggleCustomListItem(currentList.id, item.id) },
                    onDelete = { viewModel.deleteCustomListItem(currentList.id, item.id) },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 2.dp)
                )
            }

            // Add Item Chip
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(top = 6.dp, bottom = 3.dp),
                    onClick = { showAddItemDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceDark,
                        contentColor = BluePrimary
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar objeto",
                            tint = BluePrimary,
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

            // Reset checklist (uncheck all)
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 3.dp),
                    onClick = { showResetListDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceDark,
                        contentColor = MaterialTheme.colors.onBackground
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Desmarcar todos",
                            tint = Color(0xFF80D8FF),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Desmarcar todos",
                            style = MaterialTheme.typography.body2
                        )
                    }
                )
            }

            // Delete List Chip
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 3.dp),
                    onClick = { showDeleteListDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceVariantDark,
                        contentColor = Color(0xFFFF5252)
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar lista",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Eliminar lista",
                            style = MaterialTheme.typography.caption1.copy(color = Color(0xFFFF5252))
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    // Dialog: Add Item
    WearTextInputDialog(
        showDialog = showAddItemDialog,
        title = "Agregar objeto",
        presets = listOf("Cargador", "Credencial", "Botella", "Lentes", "Ropa", "Pasaporte", "Cuaderno", "Audífonos"),
        onConfirm = { name ->
            viewModel.addCustomListItem(currentList.id, name)
        },
        onDismiss = { showAddItemDialog = false }
    )

    // Dialog: Edit List Title
    WearTextInputDialog(
        showDialog = showEditTitleDialog,
        title = "Editar nombre",
        initialText = currentList.title,
        presets = listOf("Universidad", "Trabajo", "Viaje", "Gimnasio", "Compras", "Vacaciones"),
        onConfirm = { newTitle ->
            viewModel.updateCustomListTitle(currentList.id, newTitle)
        },
        onDismiss = { showEditTitleDialog = false }
    )

    // Dialog: Confirm Delete List
    ConfirmationDialog(
        showDialog = showDeleteListDialog,
        title = "¿Eliminar lista?",
        message = "Se borrará la lista \"${currentList.title}\" permanentemente.",
        confirmButtonColor = Color(0xFFFF5252),
        onConfirm = {
            viewModel.deleteCustomList(currentList.id)
            onNavigateBack()
        },
        onDismiss = { showDeleteListDialog = false }
    )

    // Dialog: Reset List
    ConfirmationDialog(
        showDialog = showResetListDialog,
        title = "¿Desmarcar todos?",
        message = "Se reiniciarán las casillas de esta lista.",
        confirmButtonColor = BluePrimary,
        onConfirm = {
            viewModel.resetCustomList(currentList.id)
        },
        onDismiss = { showResetListDialog = false }
    )
}
