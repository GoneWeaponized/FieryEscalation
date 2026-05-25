package ui_elements_misc

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
import netTools_and_related.TcpClienter
import netTools_and_related.currentPort
import netTools_and_related.currentUrl
import kotlinx.coroutines.launch

@Composable
fun LoginDialog(
    myLat: Double?,
    myLong: Double?,
    initalIp: String = "127.0.0.1",
    onLoginResult: (String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var ipInput by remember { mutableStateOf(initalIp) }
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Some Login thing idk") },
        text = {
            Column {
                Text("Connection Settings:")
                OutlinedTextField(
                    value = ipInput,
                    onValueChange = { ipInput = it },
                    label = { Text("Server IP") },
                    singleLine = true
                )
                Text("Enter username to enter the divine warmonger's cabin:")
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    singleLine = true
                )

            }
        },
        confirmButton = {
            Button(onClick = {
                scope.launch {
                    val rawIp = ipInput.trim()
                    if (rawIp.contains(":")) {
                        val parts = rawIp.split(":")
                        currentUrl = parts[0]
                        currentPort = parts[1].toIntOrNull() ?: 5010
                    } else {
                        currentUrl = "127.0.0.1"
                        currentPort = 5010
                    }

                    val result = TcpClienter.loginToServer(username, myLat, myLong)
                    onLoginResult(result, rawIp)
                }
            }) {
                Text("Connect")
            }
        })
}