package fieryTypes.entities

import fieryTypes.ResourceTypes
import fieryTypes.TypeStructure
import org.maplibre.spatialk.geojson.Position

data class StructureEntity(
    val subId: String,
    val ownedBy: String,
    val position: Position,
    val typeOf: TypeStructure,
    val hasInventory: Boolean,
    val items: List<ResourceTypes>? = null
)