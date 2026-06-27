package mapItemLoading

//import kotlinx.serialization.json.JsonObject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import backStage.locationGet
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.rememberVectorSource


@Composable
fun SymbolLayerTypeShi() {
    val context = LocalContext.current

    var myLat by remember { mutableStateOf<Double?>(null) }
    var myLong by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(Unit) {
        val locator = locationGet(context)
        locator.requestLocationUpdates { location ->
            myLat = location.latitude
            myLong = location.longitude
        }
    }


}

// just to remember this
// iconSize = interpolate(
// type = exponential(1.2f),
// input = zoom(),
// 6f to const(0f),
// 15f to const(1f), //I kinda think 1f is tad bit small... Will see about that currently imma keep it as is.
// )
// this is how i make icon scale according to zoom