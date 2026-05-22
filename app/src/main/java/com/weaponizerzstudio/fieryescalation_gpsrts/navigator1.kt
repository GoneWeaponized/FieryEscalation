package com.weaponizerzstudio.fieryescalation_gpsrts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


@Preview
@Composable
fun AppNavigation() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Gray)) {
        Box(modifier = Modifier
            .size(20.dp, 80.dp)
            .align(Alignment.BottomEnd)
            .padding(bottom = 50.dp),
            contentAlignment = Alignment.Center)
        { Text(text = "© OpenStreetMap contributors") }
    }
    val navController = rememberNavController()
    NavHost(navController, startDestination = "map1") {
        //TO be done later
        //composable(route = "Home") { CounterScr{ navController.navigate("Idk") } }
        //composable(route = "greeter") { Greeter{ navController.navigate("Home") } }
        //composable(route = "Idk") { DsiplayItems{ navController.navigate("greeter") } }
        //composable(route = "map1") {MapItem{ navController.navigate("map2") }}
        }
    }