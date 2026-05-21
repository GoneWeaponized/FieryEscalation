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
import behind_the_scenes.locationGet
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Position
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
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

    val source = rememberVectorSource("https://api.maptiler.com/data/019e3593-0d6d-7e56-a13a-c6b9c8dbd3bc/features.json?key=pPewzJdKCkilrkBYJoZx")
    // Will add dynamic coloring launch-like icon halo
    // and color depending on what the other user's object might be
    SymbolLayer(
        id = "localPlayerLayer",
        source = source,
        iconImage = image(painterResource(R.drawable.baseline_person_24), drawAsSdf = true),
        iconColor = const(Color.Green),
        iconHaloColor = const(Color.Green),
        iconHaloWidth = const(5.dp),
        iconSize = const(1f),
        minZoom = 0f,
        maxZoom = 24f,
    )
}

// just to remember this
// iconSize = interpolate(
// type = exponential(1.2f),
// input = zoom(),
// 6f to const(0f),
// 15f to const(1f), //I kinda think 1f is tad bit small... Will see about that currently imma keep it as is.
// )
// this is how i make icon scale according to zoom