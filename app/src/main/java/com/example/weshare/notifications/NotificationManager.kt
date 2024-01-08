package com.example.weshare.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner
import javax.net.ssl.HttpsURLConnection

class NotificationManager {

    fun sendMessageToFCM(fcmMessage: String) {
        val apiKey = "AIzaSyAuXbaGc5wYSgL_PKp94ksMxAIGNfdrAUk"
        val fcmUrl = "https://fcm.googleapis.com/fcm/send"

        try {
            val url = URL(fcmUrl)
            val conn = url.openConnection() as HttpsURLConnection
            conn.apply {
                doOutput = true
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "key=$apiKey")

                outputStream.write(fcmMessage.toByteArray())
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseScanner = Scanner(conn.inputStream)
                println("Response from FCM: ${responseScanner.nextLine()}")
                responseScanner.close()
            } else {
                println("Error sending FCM message. Response code: $responseCode")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestNotificationPermissionDialog() {
        val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        val context = LocalContext.current

        if (!permissionState.statis.isGranted) {
            if (permissionState.status.shouldShowRationale) {
                RationalDialog(
                    onDismissRequest = {
                        Toast.makeText(
                            context,
                            "Permission dialog dismissed",
                            Toast.LENGTH_SHORT
                        ).show
                    },
                    onConfirm = { permissionState.launchPermissionRequest() }
                )
            } else {
                PermissionDialog(
                    onRequestPermission = { permissionState.launchPermissionRequest() },
                    onDismissRequest = {
                        Toast.makeText(
                            context,
                            "Permission dialog dismissed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

     */

}