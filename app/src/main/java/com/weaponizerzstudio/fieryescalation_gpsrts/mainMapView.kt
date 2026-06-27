package com.weaponizerzstudio.fieryescalation_gpsrts
//I named it test file at first because I had no idea what I was planning to do here at first.

/* Behind the scenes is where I keep the most horrible codes
in case you, my reader, want to destroy your eyes please go ahead and read that. */

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import backStage.DataStoreManager
import backStage.locationGet
import backStage.parseTcpToEntity
import fieryEntity.PlayerEntity
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.coroutines.launch
import mapItemLoading.LayerPlayers
import mapItemLoading.updateEntityList
import netTools.FieryNetwork
import netTools.TcpClienter
import netTools.currentPort
import netTools.currentUrl
import netTools.extras.ByteCommands
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.compose.util.ClickResult
import uiElementsAndMisc.LoginDialog
import uiElementsAndMisc.UuidLoginScreen
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun MapItem(locationGet: locationGet) {
    //Here gonna define the variables.
    var showUuidDialog by remember { mutableStateOf(false) }
    var myLat by remember { mutableStateOf<Double?>(null) }
    var myLong by remember { mutableStateOf<Double?>(null) }
    var clickedPos by remember { mutableStateOf<Position?>(null)}
    val network = FieryNetwork()

    LaunchedEffect(Unit) {
        locationGet.requestLocationUpdates { location ->
            myLat = location.latitude
            myLong = location.longitude
        }
    }


    val focusManager = LocalFocusManager.current
    var theBoolThing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val scope = rememberCoroutineScope()
    val savedIP by dataStoreManager.getIp.collectAsState(initial = "")
    val savedTheme by dataStoreManager.getThemeRn.collectAsState(initial = "https://tiles.openfreemap.org/styles/fiord")
    var urlString : String by remember { mutableStateOf("") }
    val savedMeId by dataStoreManager.getMyId.collectAsState(initial = "FETCHING")
    LaunchedEffect(savedIP) { urlString = savedIP; currentUrl = savedIP}
    //will probably load JSON data
    //====================URL PATCHING================================
    val entities = remember { mutableStateListOf<PlayerEntity>() }
    LaunchedEffect(savedIP) {
        if (savedIP.isNotEmpty()) {
            if (savedIP.contains(":")) {
                val parts = savedIP.split(":")
                currentUrl = parts[0]
                currentPort = parts[1].toIntOrNull() ?: 5010
            } else {
                currentUrl = "127.0.0.1"
                currentPort = 5010
            }
        }
    }
    //================CALLING PLAYERS================================


    //I have a bad feeling because it's turning into a monolith :skull:

    Box(modifier = Modifier.fillMaxSize() .pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
        })
    }) {
        if (savedMeId != "" && savedMeId != "FETCHING") {
            //=================MAP STUFF========================================
        val cameraState =
            rememberCameraState(CameraPosition(target = Position(29.265910, 52.049274), zoom = 1.0))
        MaplibreMap(
            zoomRange = 2.5f..22f,
            baseStyle = BaseStyle.Uri(savedTheme),
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
            ),
            onMapClick = {pos, _ ->
                clickedPos = pos
               ClickResult.Consume},
        ) { //Map layer stuff loader
            //SymbolLayerTypeShi()
            LayerPlayers(entities = entities)
        }
        // TOP LAYER ROW
    Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.align(Alignment.TopCenter)) {
        Row {
            //=========================THEME SWITCH====================
            Box(
                modifier = Modifier
                .size(50.dp, 50.dp)
                .padding(top = 0.dp)
                .clickable(
                    onClick = {
                        theBoolThing = !theBoolThing
                        val newTheme = if (theBoolThing) {
                            "https://tiles.openfreemap.org/styles/fiord"
                        } else {
                            "https://tiles.openfreemap.org/styles/positron"
                        }
                        scope.launch {
                            dataStoreManager.saveTheme(newTheme)
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


            Box(
                modifier = Modifier
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
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
            //====================ZOOM=======================

            Box(
                modifier = Modifier
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
                contentAlignment = Alignment.Center
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.outline_arrows_input_24),
                    contentDescription = "Zoom in",
                    tint = Color.White
                )
            }
            //================UUID LOGIN SCREEN=============================

            Box(
                modifier = Modifier
                    .size(50.dp, 50.dp)
                    .padding(top = 0.dp)
                    .clickable(
                        onClick = { showUuidDialog = true }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(id = R.drawable.outline_id_card_24), tint = Color.White, contentDescription = "Show UUID")
            }


        }
    }
            // BOTTOM BAR

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.align(Alignment.TopCenter)) {
        Row {
            Box(
                modifier = Modifier.size(50.dp, 50.dp)
                    .clickable(
                        onClick = {

                         scope.launch {
                             try {
                                 network.sendWriter(
                                     ByteCommands.GET_PLAYERS,
                                     clickedPos?.latitude ?: 0.0,
                                     clickedPos?.longitude ?: 0.0,
                                     savedMeId
                                 )
                             } catch (e: Exception) {
                                 Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
                             }
                         }
                        }
                    )
            ) {
                Icon(painter = painterResource(id = R.drawable.baseline_add_circle_24), tint = Color.White, contentDescription = "BuildHere")
            }
        }
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
                            dataStoreManager.saveIdMe(newToken)
                            dataStoreManager.saveIp(usedIp)
                            currentUrl = usedIp } } } )}

    if (savedMeId == "FETCHING") {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center) )
    }
    if (showUuidDialog) {
        UuidLoginScreen(onDismiss = { showUuidDialog = false })
    }
    }
}
