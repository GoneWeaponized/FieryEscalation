package com.weaponizerzstudio.fieryescalation_gpsrts

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

@Preview
@Composable
fun LoginScreen() {

    var username by remember {
        mutableStateOf("")
    }

    var serverMessage by remember {
        mutableStateOf("Disconnected")
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        TextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = {
                Text("Username")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                scope.launch(Dispatchers.IO) {

                    try {

                        val socket = Socket(
                            "127.0.0.1",
                            5010
                        )

                        val writer = PrintWriter(
                            socket.getOutputStream(),
                            true
                        )

                        val reader = BufferedReader(
                            InputStreamReader(
                                socket.getInputStream()
                            )
                        )

                        // Send login
                        writer.println(
                            "LOGIN:$username"
                        )

                        // Listen forever
                        while (true) {

                            val msg =
                                reader.readLine()
                                    ?: break

                            withContext(
                                Dispatchers.Main
                            ) {
                                serverMessage = msg
                            }
                        }

                    } catch (e: Exception) {

                        withContext(
                            Dispatchers.Main
                        ) {
                            serverMessage =
                                "Error: ${e.message}"
                        }
                    }
                }
            }
        ) {
            Text("Connect")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = serverMessage
        )
    }
}