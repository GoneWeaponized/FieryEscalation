package fieryEntity

data class PlayerEntity(
    val subId: String,
    val name: String,
    val type: TypeEntity,
    val coordX: Double,
    val coordY: Double,
)