package com.weaponizerzstudio.fieryescalation_gpsrts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign.Companion.Right
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import netTools.TcpClienter


@Composable
fun CounterScr(/*onClick : ()-> Unit*/) {

    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val clickableVar = "Click to move to big list thing!"
        val scope = rememberCoroutineScope()
        var myString_w: String by remember { mutableStateOf("Welcome to Fiery Escalation") }
        var secondStringTypeE: String by remember { mutableStateOf("Command response here.") }
        Text(text = "Yo $myString_w", fontSize = 18.sp, textAlign = Right)
        Button(onClick = { scope.launch {
            myString_w = TcpClienter.fetchMessage()
        } })
        {
            Text(
                text = "CRAZY",
                textDecoration = TextDecoration.Underline
            )
        }
        Button(onClick = { scope.launch {
            secondStringTypeE = TcpClienter.fetchMessage("cmd1")
        } }) {
            Text(text = "Click to send command.")
        }
        Text(text = secondStringTypeE)
        Text(text = "")
        Text(text = clickableVar,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.clickable {
                //onClick()
            }
        )
    }
}
@Preview(showSystemUi = true)
@Composable
fun LayeredUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        //CounterScr()
        Box(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 40.dp, end = 10.dp) // Lowered padding to keep it on screen
            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .padding(8.dp), // Internal padding for the text
            contentAlignment = Alignment.Center)
        {
            Text(
                text = "© OpenStreetMap contributors",
                fontSize = 12.sp,
                color = Color.Black
            )
        }
        Box(
            modifier = Modifier
                .size(150.dp, 80.dp)
                .align(Alignment.TopEnd) // Position it at bottom center
                .padding(top = 20.dp)
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("I am floating!", color = Color.Black)
        }
    }
}