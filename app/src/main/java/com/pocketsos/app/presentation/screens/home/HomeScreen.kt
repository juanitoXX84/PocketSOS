package com.pocketsos.app.presentation.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.pocketsos.app.presentation.components.PocketScaffold
import com.pocketsos.app.presentation.components.ProgressSummaryCard
import com.pocketsos.app.presentation.theme.BluePrimary
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceDark
import com.pocketsos.app.presentation.viewmodel.PocketSosViewModel

@Composable
fun HomeScreen(
    viewModel: PocketSosViewModel,
    onNavigateToExitMode: () -> Unit,
    onNavigateToMyLists: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val exitProgress by viewModel.exitProgress.collectAsState()
    val isReady by viewModel.isExitModeReady.collectAsState()

    PocketScaffold(listState = listState) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Header
            item {
                Text(
                    text = "Pocket SOS",
                    style = MaterialTheme.typography.title1.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = GreenPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }

            // Subtitle / status
            item {
                Text(
                    text = "Tu asistente antes de salir",
                    style = MaterialTheme.typography.caption2,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Active list progress indicator (if items exist)
            if (exitProgress.second > 0) {
                item {
                    ProgressSummaryCard(
                        completedCount = exitProgress.first,
                        totalCount = exitProgress.second,
                        title = "Salida actual",
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .padding(bottom = 6.dp)
                    )
                }
            }

            // Primary Button: "Modo salida"
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 4.dp),
                    onClick = onNavigateToExitMode,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = GreenPrimary,
                        contentColor = Color.Black
                    ),
                    icon = {
                        Icon(
                            imageVector = if (isReady) Icons.Filled.CheckCircle else Icons.Filled.PlayArrow,
                            contentDescription = "Modo salida",
                            tint = Color.Black,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Modo salida",
                            style = MaterialTheme.typography.title3.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                    },
                    secondaryLabel = {
                        Text(
                            text = if (isReady) "¡Todo listo! Toca para revisar" else "${exitProgress.first}/${exitProgress.second} objetos listos",
                            style = MaterialTheme.typography.caption2,
                            color = Color.Black.copy(alpha = 0.75f)
                        )
                    }
                )
            }

            // Button: "Mis listas"
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 3.dp),
                    onClick = onNavigateToMyLists,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceDark,
                        contentColor = MaterialTheme.colors.onBackground
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                            contentDescription = "Mis listas",
                            tint = BluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Mis listas",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                )
            }

            // Button: "Recordatorios"
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 3.dp),
                    onClick = onNavigateToReminders,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceDark,
                        contentColor = MaterialTheme.colors.onBackground
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Alarm,
                            contentDescription = "Recordatorios",
                            tint = Color(0xFFFFB74D),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Recordatorios",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                )
            }

            // Button: "Historial"
            item {
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(vertical = 3.dp),
                    onClick = onNavigateToHistory,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = SurfaceDark,
                        contentColor = MaterialTheme.colors.onBackground
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = "Historial",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Historial",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
