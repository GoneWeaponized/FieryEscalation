package mapItemLoading

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Geometry
import org.maplibre.spatialk.geojson.Point

@Composable
fun LayerPlayers() {
    val players = entityParser.players
    val pSource = rememberGeoJsonSource(
        data = GeoJsonData.Features(
            FeatureCollection<Geometry, JsonObject?>(
                features = players.filter { !it.position.latitude.isNaN() && !it.position.longitude.isNaN() }.map { player ->
                    Feature(
                        geometry = Point(player.position),
                        properties = buildJsonObject {
                            put("subId", JsonPrimitive(player.subId))
                            put("name", JsonPrimitive(player.name))
                        }
                    )
                }
            )
        )
    )
    SymbolLayer(
        id = "PlayerLayer",
        source = pSource,
        iconImage = image(painterResource(R.drawable.baseline_person_24), drawAsSdf = true),
        iconColor = const(Color.White),
        iconSize = const(1.5f),
        iconHaloWidth = const(8.dp),
        iconHaloColor = const(Color.Red)
    )
}

//    val geoJsonData = remember(entities.toList()) {
//        val featureCollection = FeatureCollection(
//            features = entities.map { entity ->
//                Feature(
//                    // MapLibre uses [Longitude, Latitude]
//                    // If coordX is Lat and coordY is Long, this is correct.
//                    geometry = Point(Position(entity.coordY, entity.coordX)),
//                    id = entity.subId
//                )
//            }
//        )
//        val data = GeoJsonData.Features(featureCollection)
//        data
//    }
//
//    val sourceP = rememberGeoJsonSource(
//        data = geoJsonData
//    )
//
//    SymbolLayer(
//        id = "player-entities-layer",
//        source = sourceP,
//        iconImage = image(painterResource(R.drawable.baseline_person_24), drawAsSdf = true),
//        iconColor = const(Color.White), // Changed to White bec its base for unowned entity
//        iconAllowOverlap = const(true),
//        iconSize = const(2.0f), // Made slightly larger to see easier (temporary change)
//        iconIgnorePlacement = const(true),
//        iconHaloColor = const(Color.Yellow),
//        iconHaloWidth = const(5.dp),
//        onClick = { features ->
//            val chosenFeature = features.firstOrNull()
//            val entityId = chosenFeature?.id
//            ClickResult.Consume
//        }
//    )
//}
//
//fun updateEntityList(entities: SnapshotStateList<PlayerEntity>, newPlayerEntity: PlayerEntity) {
//    val index = entities.indexOfFirst { it.subId == newPlayerEntity.subId }
//    if (index != -1) {
//        entities[index] = newPlayerEntity // Updates existing player
//    } else {
//        entities.add(newPlayerEntity)    // Adds new player
//    }
//}