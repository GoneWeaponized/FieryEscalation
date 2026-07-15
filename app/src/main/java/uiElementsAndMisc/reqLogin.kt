package uiElementsAndMisc

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.weaponizerzstudio.fieryescalation_gpsrts.network
import netTools.currentPort
import netTools.currentUrl
import kotlinx.coroutines.launch
import netTools.extras.ByteCommands

@Composable
fun LoginDialog(
    myLat: Double?,
    myLong: Double?,
    initialIp: String = "127.0.0.1"
) {
    var username by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var ipInput by remember { mutableStateOf(initialIp) }

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Create a FEscalation User") },
        text = {
            Column {
                Text("Connection Settings:")
                OutlinedTextField(
                    value = ipInput,
                    onValueChange = { ipInput = it },
                    label = { Text("Server IP") },
                    singleLine = true
                )
                Text("Enter username: (make sure location adn perms are enabled)")
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    scope.launch {
                        val rawIp = ipInput.trim()
                        if (rawIp.contains(":")) {
                            val parts = rawIp.split(":")
                            currentUrl = parts[0]
                            currentPort = parts[1].toIntOrNull() ?: 5010
                        } else {
                            currentUrl = rawIp
                            currentPort = 5010
                        }
                        if (myLat != null && myLong != null) {
                            try {
                                if (!network.isConnected) {
                                    network.connect()
                                }
                                network.loginWrite(username, myLat, myLong, ByteCommands.LOGIN)
                            } catch (e: Exception) {
                                Log.e("LoginError", "Error during login: ${e.message}")
                            }
                        }
                    }
                },
                enabled = username.isNotBlank() && myLat != null && myLong != null
            ) {
                Text("Connect")
            }
        })
}