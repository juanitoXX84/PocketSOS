package com.pocketsos.app.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Dialog
import com.pocketsos.app.presentation.theme.GreenPrimary
import com.pocketsos.app.presentation.theme.SurfaceVariantDark

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title: String,
    message: String = "",
    confirmButtonColor: Color = GreenPrimary,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        showDialog = showDialog,
        onDismissRequest = onDismiss
    ) {
        Alert(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.title3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = SurfaceVariantDark,
                        contentColor = MaterialTheme.colors.onSurface
                    ),
                    modifier = Modifier.size(ButtonDefaults.DefaultButtonSize)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancelar"
                    )
                }
            },
            positiveButton = {
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = confirmButtonColor,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.size(ButtonDefaults.DefaultButtonSize)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Confirmar"
                    )
                }
            }
        ) {
            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
