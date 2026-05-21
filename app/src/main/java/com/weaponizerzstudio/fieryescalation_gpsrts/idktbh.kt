package com.weaponizerzstudio.fieryescalation_gpsrts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

//@Preview (showSystemUi = true)
@Composable

fun Greeter(name:String = "Lol", onColick : ()-> Unit) {
    Column(verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Hello there $name")
        Button(onClick = { onColick() }) {
            Text(text = "Click to go back.")
        }
    }
}