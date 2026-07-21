package com.weaponizerzstudio.fieryescalation_gpsrts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import backStage.viewModels.NetworkViewModel
import netTools.extras.ByteCommands

/*
Here I will be doing my login screen that contains the following:
1. IP address field
2. Port field
3. Connect button
4. Text view that updates the user on what is happening.

Also, remember this is more of a debug login screen, not gon add email and password stuff
Just a login ID and Account backup code that will be generated - but that too is to be done later
 */

@Preview(showSystemUi = true)
@Composable
fun LoginScreen0() {
    LaunchedEffect(Unit) {
        NetworkViewModel().connect()
    }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "ji there", modifier = Modifier.size(60.dp).width(80.dp).height(80.dp))
            Button(onClick = {
                NetworkViewModel().send(ByteCommands.GET_PLAYERS, lt = 45.878348, ln = 37.872343, uu = "f28fb523-a082-4184-9591-be4959c939b2")
            }) { Text(text = "CLICK") }
        }
}