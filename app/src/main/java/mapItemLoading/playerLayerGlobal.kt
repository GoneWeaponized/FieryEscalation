package mapItemLoading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import kotlinx.serialization.json.JsonObject
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.Geometry
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position

@Composable
fun LayerOfPlayers() {
    val features = remember { mutableStateListOf<Feature<Geometry, JsonObject?>>() }
    fun addFeature(position: Position) {
        features.add(Feature(geometry = Point(position), properties = null))
    }
}