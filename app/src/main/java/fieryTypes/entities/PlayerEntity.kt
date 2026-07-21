package fieryTypes.entities

import fieryTypes.TypeEntity
import org.maplibre.spatialk.geojson.Position

data class PlayerEntity(
    val subId: String,
    var name: String,
    val type: TypeEntity, // useless for now
    var position: Position // long, lat
)