package com.weaponizerzstudio.fieryescalation_gpsrts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Preview
@Composable
fun StartScreen() {

    var connectionState by remember { mutableStateOf("Connecting") }

    LaunchedEffect(Unit) {

        network.connect()

        while (true) {

            connectionState = when {
                network.isConnected -> "CONNECTED OMG!"
                else -> "CAN'T CONNECT"
            }

            delay(500.milliseconds) // check twice a second
        }
    }

    Column(
        modifier = Modifier
            .background(Color(0xFF2A2529))
            .fillMaxSize()
    ) {

        Text(
            text = "Current Connection state:",
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(30.dp)
        )

        Text(
            text = connectionState,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            color = when (connectionState) {
                "Connecting" -> Color(0xFF8CA4AE)
                "CONNECTED OMG!" -> Color(0xFF31FF3F)
                "CAN'T CONNECT" -> Color(0xFFC50022)
                else -> Color.White
            },
            modifier = Modifier.padding(start = 30.dp)
        )

    }
}