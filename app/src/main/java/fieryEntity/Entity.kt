package fieryEntity

data class Entity(
    val id: String,
    val name: String,
    val ownedBy: String,
    val type: EntityType,
    val coordX: Double,
    val coordY: Double,
)