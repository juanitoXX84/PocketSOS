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
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import com.pocketsos.app.presentation.components.PocketScaffold
import com.pocketsos.app.presentation.components.WearTextInputDialog
import com.pocketsos.app.presentation.theme.BluePrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.theme.SurfaceVariantDark
import com.pocketsos.app.presentation.viewmodel.PocketSosViewModel

@Composable
fun MyListsScreen(
    viewModel: PocketSosViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToListDetail: (String) -> Unit
) {
    val listState = rememberScalingLazyListState()
    val customLists by viewModel.customLists.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

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
                        text = "Mis listas",
                        style = MaterialTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
                        color = BluePrimary
                    )
                }
            }

            // Create new list button
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 4.dp),
                    onClick = { showCreateDialog = true },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = BluePrimary.copy(alpha = 0.2f),
                        contentColor = BluePrimary
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Nueva lista",
                            tint = BluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "+ Nueva lista",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                )
            }

            // Custom lists
            items(customLists, key = { it.id }) { list ->
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 2.dp),
                    onClick = { onNavigateToListDetail(list.id) },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceDark,
                        contentColor = MaterialTheme.colors.onBackground
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                            contentDescription = list.title,
                            tint = BluePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = list.title,
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 1
                        )
                    },
                    secondaryLabel = {
                        Text(
                            text = "${list.completedCount}/${list.totalCount} objetos listos",
                            style = MaterialTheme.typography.caption2,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    WearTextInputDialog(
        showDialog = showCreateDialog,
        title = "Nombre de la lista",
        presets = listOf("Gimnasio", "Compras", "Camping", "Playa", "Fiesta", "Ciclismo", "Mascotas"),
        onConfirm = { name ->
            viewModel.createCustomList(name)
        },
        onDismiss = { showCreateDialog = false }
    )
}
