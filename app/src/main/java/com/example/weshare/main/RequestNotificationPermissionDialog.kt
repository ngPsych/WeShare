package com.example.weshare.main

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
class RequestNotificationPermissionDialog {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestNotificationPermissionDialog() {
        val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        val openDialog = remember { mutableStateOf(true) }

        if (!permissionState.status.isGranted && openDialog.value) {
            AlertDialog(
                backgroundColor = Color.LightGray,
                modifier = Modifier.fillMaxSize(0.5F),
                onDismissRequest = { openDialog.value = false },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text(text = "Dismiss")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        permissionState.launchPermissionRequest()
                        openDialog.value = false
                    }) {
                        Text(text = "Confirm")
                    }
                },
                title = { Text(text = "Grant permission to notifications")})
        }
    }
}