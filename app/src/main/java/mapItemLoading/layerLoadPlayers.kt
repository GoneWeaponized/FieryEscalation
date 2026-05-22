package mapItemLoading

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fieryEntity.Entity
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Position
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource

@Composable
fun LayerPlayers(entities: SnapshotStateList<Entity>) {
    // 1. Generate GeoJSON and Log it
    val geoJsonData = remember(entities.toList()) {
        val featureCollection = FeatureCollection(
            features = entities.map { entity ->
                Feature(
                    // MapLibre uses [Longitude, Latitude]
                    // If coordX is Lat and coordY is Long, this is correct.
                    geometry = Point(Position(entity.coordY, entity.coordX)),
                    id = entity.id
                )
            }
        )
        val data = GeoJsonData.Features(featureCollection)

        // LOGGING: This will show you exactly what is being sent to the map
        Log.d("MAP_RENDER", "GeoJSON Update: ${entities.size} entities")
        entities.forEach {
            Log.d("MAP_RENDER", "Entity: ${it.id} at [Long:${it.coordY}, Lat:${it.coordX}]")
        }

        data
    }

    // 2. Use a unique key for the source to force refresh
    // We use entities.size as a simple key, or entities.hashCode()
    val sourceP = rememberGeoJsonSource(
        data = geoJsonData
    )

    SymbolLayer(
        id = "player-entities-layer",
        source = sourceP,
        // Ensure this drawable exists and is valid
        iconImage = image(painterResource(R.drawable.baseline_person_24), drawAsSdf = true),
        iconColor = const(Color.White), // Changed to Red for better visibility during debugging
        iconAllowOverlap = const(true),
        iconSize = const(2.0f), // Made slightly larger to see easier
        iconIgnorePlacement = const(true),
        iconHaloColor = const(Color.Yellow),
        iconHaloWidth = const(5.dp)
    )
}

fun updateEntityList(entities: SnapshotStateList<Entity>, newEntity: Entity) {
    val index = entities.indexOfFirst { it.id == newEntity.id }
    if (index != -1) {
        entities[index] = newEntity // Updates existing player
    } else {
        entities.add(newEntity)    // Adds new player
    }
}