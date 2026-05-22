package behind_the_scenes

import fieryEntity.Entity
import fieryEntity.EntityType

fun parseTcpToEntity(data: String): Entity? {
    return try {
        // Expected format: entity_id|entity_owner_Id|coordx|coordy
        val parts = data.split("|")
        if (parts.size >= 4) {
            Entity(
                id = parts[0],
                ownedBy = parts[1],
                type = EntityType.PLAYER,    // Defaulting to PLAYER for this stream
                coordX = parts[2].toDouble(),
                coordY = parts[3].toDouble(),
                name = "default - Player"

            )
        } else null
    } catch (e: Exception) {
        null
    }
}