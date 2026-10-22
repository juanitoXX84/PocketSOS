package com.pocketsos.app.presentation.screens.history

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.CheckCircleOutline
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
import com.pocketsos.app.presentation.components.ConfirmationDialog
import com.pocketsos.app.presentation.components.PocketScaffold
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.theme.SurfaceVariantDark
import com.pocketsos.app.presentation.viewmodel.PocketSosViewModel

@Composable
fun HistoryScreen(
    viewModel: PocketSosViewModel,
    onNavigateBack: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val history by viewModel.history.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

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
                        text = "Historial",
                        style = MaterialTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }

            // Empty state
            if (history.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.35f),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Aún no tienes salidas registradas",
                            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Completa tu checklist en Modo salida y pulsa \"Finalizar salida\" para registrarla.",
                            style = MaterialTheme.typography.caption2,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Clear history button
                item {
                    Chip(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .padding(vertical = 3.dp),
                        onClick = { showClearDialog = true },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = SurfaceVariantDark,
                            contentColor = Color(0xFFFF5252)
                        ),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Vaciar historial",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Vaciar historial",
                                style = MaterialTheme.typography.caption1.copy(color = Color(0xFFFF5252))
                            )
                        }
                    )
                }

                // History entries
                items(history, key = { it.id }) { entry ->
                    val isAllDone = entry.totalCount > 0 && entry.completedCount == entry.totalCount

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceDark)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (isAllDone) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
                                contentDescription = null,
                                tint = if (isAllDone) GreenPrimary else Color(0xFF80D8FF),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = entry.listName,
                                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${entry.completedCount}/${entry.totalCount} listos",
                                        style = MaterialTheme.typography.caption2.copy(
                                            color = if (isAllDone) GreenPrimary else MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "• ${entry.formattedDate}",
                                        style = MaterialTheme.typography.caption2.copy(
                                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    // Dialog: Confirm Clear History
    ConfirmationDialog(
        showDialog = showClearDialog,
        title = "¿Vaciar historial?",
        message = "Se borrarán todos los registros de salidas anteriores.",
        confirmButtonColor = Color(0xFFFF5252),
        onConfirm = {
            viewModel.clearHistory()
        },
        onDismiss = { showClearDialog = false }
    )
}
