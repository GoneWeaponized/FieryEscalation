package com.weaponizerzstudio.fieryescalation_gpsrts
//I named it test file at first because I had no idea what I was planning to do here at first.

/* Behind the scenes is where i keep the most horrible codes
in case you, my reader, want to destroy your eyes please go ahead and read that. */

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import behind_the_scenes.DataStoreManager
import behind_the_scenes.locationGet
import behind_the_scenes.parseTcpToEntity
import fieryEntity.Entity
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.coroutines.launch
import mapItemLoading.LayerPlayers
import mapItemLoading.updateEntityList
import netTools_and_related.TcpClienter
import netTools_and_related.currentPort
import netTools_and_related.currentUrl
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import ui_elements_misc.LoginDialog

@Composable
fun MapItem(locationGet: locationGet) {
    //Here gon define the variables.
    var myLat by remember { mutableStateOf<Double?>(null) }
    var myLong by remember { mutableStateOf<Double?>(null) }
    LaunchedEffect(Unit) {
        locationGet.requestLocationUpdates { location ->
            myLat = location.latitude
            myLong = location.longitude
        }
    }
    var mapStyleVal1: String by remember { mutableStateOf("https://tiles.openfreemap.org/styles/fiord") }
    var theBoolThing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val scope = rememberCoroutineScope()
    val savedIP by dataStoreManager.getIp.collectAsState(initial = "")
    //val savedBool by dataStoreManager.getIp.collectAsState(initial = false)
    var urlString : String by remember { mutableStateOf("") }
    val savedMeId by dataStoreManager.getMyId.collectAsState(initial = "FETCHING")
    LaunchedEffect(savedIP) { urlString = savedIP; currentUrl = savedIP}
    //will probably load json data
    val entities = remember { mutableStateListOf<Entity>() }
    LaunchedEffect(savedIP) {
        if (savedIP.isNotEmpty()) {
            urlString = savedIP
            currentUrl = savedIP
            Log.d("TCP", "Global IP updated to: ${currentUrl}")
        }
    }
    Log.d("saved ID dump", savedMeId)

    //=================SAVING IP ADDRESS=============================
    LaunchedEffect(currentUrl, savedMeId) {
        while(true) {
            try {
                if (myLat != null && myLong != null && savedMeId != "FETCHING" && savedMeId != "") {
                    val command = "GET_PLAYERS|$savedMeId|$myLat|$myLong"
                    val response = TcpClienter.fetchMessage(command)
                    Log.d("TCP DATA DUMP","Recieved: $response")
                    val newEntity = parseTcpToEntity(response)
                    if (newEntity != null) {
                        updateEntityList(entities, newEntity)
                        Log.d("ENTI NOT NULL DUMP", "Entity COUNT: ${entities.size}")
                    }

                }
            } catch (e: Exception) {
                Log.d("hello","$e")
            }
            kotlinx.coroutines.delay(1000) //idk lets go with 10 second delay
            //will make it update better but launch does it per second so idk since the
            //main goal is always local player global ones are not so important
        }
    }

    //I have a bad feeling because it's turning into a monolith :skull:

    Box(modifier = Modifier.fillMaxSize()) {
        if (savedMeId != "" && savedMeId != "FETCHING") {
        val cameraState =
            rememberCameraState(CameraPosition(target = Position(29.265910, 52.049274), zoom = 1.0))
        MaplibreMap(
            zoomRange = 2.5f..22f,
            baseStyle = BaseStyle.Uri(mapStyleVal1),
            cameraState = cameraState,
            styleState = rememberStyleState(),
            options = MapOptions(
                ornamentOptions = OrnamentOptions(
                    isCompassEnabled = false,
                    isLogoEnabled = true,
                    isAttributionEnabled = true,
                    isScaleBarEnabled = false,
                ),
                gestureOptions = GestureOptions(
                    isTiltEnabled = false,
                    isZoomEnabled = true,
                    isRotateEnabled = false,
                    isScrollEnabled = true,
                ),
                renderOptions = RenderOptions(
                    RenderOptions.RenderMode.SurfaceView
                ),
            )
        ) { //Map layer stuff loader
            //SymbolLayerTypeShi()
            LayerPlayers(entities = entities)
        }
        // TOP LAYER ROW
        Row {
            //=========================THEME SWITCH====================
            Box(modifier = Modifier
                .size(50.dp, 50.dp)
                .padding(top = 0.dp)
                .clickable(
                    onClick = {
                        theBoolThing = !theBoolThing
                        mapStyleVal1 = if (theBoolThing) {
                            "https://tiles.openfreemap.org/styles/fiord"
                        } else {
                            "https://tiles.openfreemap.org/styles/positron"
                        }
                    }
                )
                //.align(Alignment.TopStart)
                .background(Color.DarkGray.copy(0.3f), shape = RoundedCornerShape(0.dp)),
                contentAlignment = Alignment.Center) {
                //Text(text = "Change Theme", color = Color.White)
                Icon(
                    painter = painterResource(id = R.drawable.outline_brightness_6_24),
                    contentDescription = "Change Theme",
                    tint = Color.White
                )
            }
            // ========================IP CONNECT================


            Box(modifier = Modifier
                .weight(1f)
                .size(50.dp)
                .background(Color.DarkGray.copy(0.3f)),
                contentAlignment = Alignment.Center
                //align(Alignment.TopCenter)
            ) {

                OutlinedTextField(
                    value = urlString,
                    onValueChange = { newValue ->
                        urlString = newValue

                        // Handle "host:port" format
                        if (newValue.contains(":")) {
                            val parts = newValue.split(":")
                            currentUrl = parts[0] // e.g. 0.tcp.eu.ngrok.io
                            currentPort = parts[1].toIntOrNull() ?: 5010 // e.g. 12345
                        } else {
                            currentUrl = "127.0.0.1"
                            currentPort = 5010
                        }

                        scope.launch {
                            dataStoreManager.saveIp(newValue) // Save the full string (e.g. host:port)
                        }
                    },
                    // ...
                )
            }
            //====================ZOOM=======================

            Box(modifier = Modifier
                .size(50.dp, 50.dp)
                .clickable(
                    onClick = {
                        locationGet.getLastLoc { location ->
                            location?.let {
                                cameraState.position = CameraPosition(
                                    target = Position(location.longitude, location.latitude),
                                    zoom = 15.0
                                )
                            }
                        }
                    })
                //.align(Alignment.TopEnd)
                .padding(top = 0.dp)
                .background(
                    Color.DarkGray.copy(0.3f), shape = RoundedCornerShape(0.dp)
                ),
                contentAlignment = Alignment.Center)
            {
                Icon(
                    painter = painterResource(id = R.drawable.outline_arrows_input_24),
                    contentDescription = "Zoom in",
                    tint = Color.White
                )
            }


        }
        //=======================ATTRIBUTION===================================

        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 5.dp, end = 10.dp) // Lowered padding to keep it on screen
            .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(4.dp), // Internal padding for the text
            contentAlignment = Alignment.Center)
        {
            Text(
                text = "© OpenStreetMap contributors",
                fontSize = 12.sp,
                color = Color.Black
            )
            Log.d("ID change:", savedMeId)
            Log.d("Lat Long dump","$myLat, $myLong")
        }
    } //saved id login !true then:
        if (savedMeId == "") {
            LoginDialog(
                myLat = myLat,
                myLong = myLong,
                initalIp = savedIP,
                onLoginResult = { newToken, usedIp -> // Receives the token and the IP
                    if (!newToken.startsWith("Error")) {
                        scope.launch {
                            // Save both to persistence
                            dataStoreManager.saveIdMe(newToken)
                            dataStoreManager.saveIp(usedIp)

                            // Ensure global var is updated for future TCP loops
                            currentUrl = usedIp
                        }
                    }
                }
                )
        }
    if (savedMeId == "FETCHING") {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center) )
    }
    }
}
