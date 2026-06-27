package backStage

import fieryEntity.PlayerEntity
import fieryEntity.TypeEntity

fun parseTcpToEntity(data: String): PlayerEntity? {
    return try {

        val parts = data.split("|")
        if (parts.size >= 4) {
            PlayerEntity(
                subId = parts[0],
                name = parts[1],
                type = TypeEntity.PLAYER,
                coordX = parts[2].toDouble(),
                coordY = parts[3].toDouble()
            )
        } else null
    } catch (e: Exception) {
        null
    }
}