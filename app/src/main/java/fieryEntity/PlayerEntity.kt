package fieryEntity

import org.maplibre.spatialk.geojson.Position

data class PlayerEntity(
    val subId: String,
    var name: String,
    val type: TypeEntity,
    var position: Position
)