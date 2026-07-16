package fieryEntity.parseOperations

import androidx.compose.runtime.mutableStateListOf
import fieryEntity.PlayerEntity
import fieryEntity.TypeEntity
import org.maplibre.spatialk.geojson.Position
import java.nio.ByteBuffer

class EntityParser {

    val players = mutableStateListOf<PlayerEntity>()
    val subIDlength= ByteArray(16)

    /*
    data structure:
    double for lat
    double for long
    16 character subID
    name length Uint16BE
    name

     */
    fun parsePlayer(data: ByteBuffer): PlayerEntity {
        val latitude = data.getDouble()
        val longitude = data.getDouble()
        data.get(subIDlength)
        val subID = String(subIDlength, Charsets.UTF_8).trimEnd { it == '\u0000' }
        val nLen = data.getShort().toInt() and 0xFFFF
        val nameBytes = ByteArray(nLen)
        data.get(nameBytes)
        val name = String(nameBytes, Charsets.UTF_8)

        return PlayerEntity(
            subId = subID,
            name = name,
            type = TypeEntity.PLAYER,
            position = Position(longitude = longitude, latitude)
        )
    }

    fun parseAllPlayers(data: ByteBuffer): List<PlayerEntity> {
        while (data.remaining() >= 34) /*since 34 is the least that can happen*/ {

            val newPlayer = parsePlayer(data)

            if (newPlayer.position.latitude.isNaN() || newPlayer.position.longitude.isNaN()) {
                continue
            }

            val index = players.indexOfFirst { it.subId == newPlayer.subId }

            if (index != -1) {
                players[index] = newPlayer
            } else {
                players.add(newPlayer)
            }
        }
        return players
    }
}