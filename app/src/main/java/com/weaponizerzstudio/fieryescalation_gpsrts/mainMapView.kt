package com.weaponizerzstudio.fieryescalation_gpsrts

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import backStage.DataStoreManager
import backStage.LocationGet
import backStage.viewModels.NetworkViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mapItemLoading.LayerOfPlayers
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
import org.maplibre.spatialk.geojson.Position
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.maplibre.spatialk.geojson.Feature
import uiElementsAndMisc.EntityDetailView
import uiElementsAndMisc.LoginDialog
import uiElementsAndMisc.UuidLoginScreen
import uiElementsAndMisc.bottomBar.BottomBar
import uiElementsAndMisc.bottomBar.InventoryViewUI
import fieryTypes.parseOperations.InvParse
import kotlin.time.Duration.Companion.seconds

val network = NetworkViewModel()

@SuppressLint("SuspiciousIndentation")
@Composable
fun MapItem(locationGet: LocationGet) {
    //Here gonna define the variables.
    var showUuidDialog by remember { mutableStateOf(false) }
    var myLat by remember { mutableStateOf<Double?>(null) }
    var myLong by remember { mutableStateOf<Double?>(null) }
    var clickedPos by remember { mutableStateOf<Position?>(null)}
    var theBoolThing by remember { mutableStateOf(false) }
    var clickedPlayer by remember { mutableStateOf<Feature<*, JsonObject?>?>(null) }
    var showInventory by remember { mutableStateOf(false) }

    val connectionState by network.connectionState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val scope = rememberCoroutineScope()
    val savedIP by dataStoreManager.getIp.collectAsState(initial = "")
    val savedTheme by dataStoreManager.getThemeRn.collectAsState(initial = "$currentUrl/get-map")
    var urlString : String by remember { mutableStateOf("") }
    val savedUUID by dataStoreManager.getMyId.collectAsState(initial = "FETCHING")
    val focusManager = LocalFocusManager.current

    //====================URL PATCHING================================
    LaunchedEffect(savedIP) {
        urlString = savedIP
        if (savedIP.isNotEmpty()) {
            if (savedIP.contains(":")) {
                val parts = savedIP.split(":")
                currentUrl = parts[0]
                currentPort = parts[1].toIntOrNull() ?: 5010
            } else {
                currentUrl = savedIP
                currentPort = 5010
            }
        }
    }

    //============ LOCATION ==========================
    LaunchedEffect(Unit) {
        locationGet.requestLocationUpdates { location ->
            myLat = location.latitude
            myLong = location.longitude } }

    //============ NETWORK INITIALIZATION ==========================
    LaunchedEffect(connectionState) {
        if (connectionState) {
            try {
                network.readStart()

            } catch (e: Exception) {
                Log.e("NetworkRead", "Error in readStart: ${e.message}")
            }
        }
    }

    LaunchedEffect(savedUUID) {
        if (savedUUID != "" && savedUUID != "FETCHING") {
            network.connect()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (savedUUID != "" && savedUUID != "FETCHING") {
        val cameraState = rememberCameraState(CameraPosition(target = Position(29.265910, 52.049274), zoom = 2.5))
            LaunchedEffect(savedUUID, connectionState) {
                if (connectionState) {
                    Log.d("network state: ", connectionState.toString())
                    while (true) {
                        val lat = myLat
                        val lng = myLong
                        if (lat != null && lng != null) {
                            network.send(ByteCommands.GET_PLAYERS, lt = lat, ln = lng, uu = savedUUID)
                            network.loginEvn(ByteCommands.REQ_INV, uu = savedUUID)
                        }
                        delay(1.seconds)
                    }
                }
            }
            //=================MAP STUFF========================================
            MaplibreMap(
            zoomRange = 2.5f..22f,
            baseStyle = BaseStyle.Uri(savedTheme),
            cameraState = cameraState,
            styleState = rememberStyleState(),
            modifier = Modifier,
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
                Log.d("MapClick", "Map background clicked at $pos")
                focusManager.clearFocus()
                clickedPos = pos
                clickedPlayer = null
                scope.launch { cameraState.animateTo(finalPosition = CameraPosition(target = Position(pos.longitude, pos.latitude), zoom = cameraState.position.zoom),
                    duration = 0.65.seconds
                )} //Launch style click to move to. Implementation of dynamic zoom values later.

               ClickResult.Pass},
        )
        // MAP LAYERS
        { 
            LayerOfPlayers(onPlayerClick = { 
                Log.d("MapClick", "Player clicked: ${it?.properties}")
                clickedPlayer = it 
            })
        }
        // TOP ROW
        // To add: 1. Connection, zoom button, ID manager button, theme change, money , etc...
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
                            "https://5df8-2409-40c4-2032-5dbd-96da-f746-8772-b705.ngrok-free.app/get-map-style"
                        }
                        scope.launch { dataStoreManager.saveTheme(newTheme)}})
                .background(Color.DarkGray.copy(0.3f), shape = RoundedCornerShape(0.dp)),
                contentAlignment = Alignment.Center) {
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
                    .background(
                        Color.DarkGray.copy(0.3f), shape = RoundedCornerShape(0.dp))
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
        Box(Modifier.align(Alignment.BottomStart)) {
            BottomBar(
                onInventoryClick = { showInventory = !showInventory },
                isInventoryOpen = showInventory
            )
        }
    } //saved id login !true then:
        if (savedUUID == "") {
            LoginDialog(
                myLat = myLat,
                myLong = myLong,
                initialIp = savedIP
            )}

        if (savedUUID == "FETCHING") {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center) )
        }
        if (showUuidDialog) {
            UuidLoginScreen(onDismiss = { showUuidDialog = false })
        }

        if (showInventory) {
            // This Box acts as a "scrim" or overlay that closes the inventory when you click anywhere else
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showInventory = false }
            )

            val inventoryData by InvParse.instance.inventory.collectAsStateWithLifecycle()
            InventoryViewUI(
                data = inventoryData,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 125.dp)
                    .heightIn(max = 400.dp) // Constrain height so it scrolls if many items
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Consuming click to prevent closing when clicking the UI itself */ },
                onItemClick = { resource ->
                    Log.d("Inventory", "Clicked on ${resource.type.name}")
                }
            )
        }

        if (clickedPlayer != null && !showInventory) {
            val name = clickedPlayer?.properties?.get("name")?.jsonPrimitive?.content ?: "Unknown"
            EntityDetailView(
                name = name,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp)
            )
        }
    } // HERE ends the BOX
}
