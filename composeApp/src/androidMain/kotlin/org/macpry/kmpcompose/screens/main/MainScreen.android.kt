package org.macpry.kmpcompose.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
actual fun NotificationPermissionDialog() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        CheckNotificationPermission()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CheckNotificationPermission() {
    val permissionState =
        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        PermissionDialog(
            if (permissionState.status.shouldShowRationale) "Rationale" else "Permission"
        ) {
            permissionState.launchPermissionRequest()
        }
    }
}

@Composable
fun PermissionDialog(title: String, onRequestPermission: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            onDismissRequest = { showWarningDialog = false },
            title = { Text(text = "Please allow for notifications") },
            text = { Text(title) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRequestPermission()
                        showWarningDialog = false

                    }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showWarningDialog = false
                    }) {
                    Text("Cancel", style = TextStyle(color = Color.Gray))
                }
            },
        )
    }
}
