package mapItemLoading

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fieryEntity.parseOperations.EntityParser
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import fieryEntity.TypeEntity
import kotlinx.serialization.json.jsonPrimitive
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Geometry
import org.maplibre.spatialk.geojson.Point
import uiElementsAndMisc.EntityDetailView

val entityParser = EntityParser()

@Composable
fun LayerOfPlayers(onPlayerClick: (Feature<*, JsonObject?>?) -> Unit) {
    val players = entityParser.players
    var selectedFeatureId by remember { mutableStateOf<Any?>(null) }
    val features = players.filter { !it.position.latitude.isNaN() && !it.position.longitude.isNaN() }.map { player ->
        Feature<Geometry, JsonObject?>(
            geometry = Point(player.position),
            properties = buildJsonObject {
                put("subId", JsonPrimitive(player.subId))
                put("name", JsonPrimitive(player.name))
            }
        )
    }
    val featureCol = FeatureCollection(features = features)
    val pDataSource = rememberGeoJsonSource(GeoJsonData.Features(featureCol))
    SymbolLayer(
        id = "Players",
        source = pDataSource,
        iconImage = image(painterResource(R.drawable.baseline_person_24), drawAsSdf = true),
        iconColor = const(Color.White),
        iconSize = const(1.5f),
        iconHaloWidth = const(8.dp),
        iconHaloColor = const(Color.Red),
        iconAllowOverlap = const(true),
        iconIgnorePlacement = const(true),
        onClick = { features ->
            onPlayerClick(features.firstOrNull())

            ClickResult.Consume
        }
    )
}
