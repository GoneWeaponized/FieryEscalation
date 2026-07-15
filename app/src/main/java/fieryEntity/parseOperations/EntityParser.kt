package fieryEntity.parseOperations

import fieryEntity.PlayerEntity
import fieryEntity.TypeEntity
import kotlinx.io.bytestring.getByteString
import java.nio.ByteBuffer

class EntityParser {
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
            coordX = latitude,
            coordY = longitude
        )
    }

    fun parseAllPlayers(data: ByteBuffer): List<PlayerEntity> {
        val players = mutableListOf<PlayerEntity>()

        //fixed part is 34 since that's the least which would arrive.
        while (data.remaining() >= 34) {
            players.add(parsePlayer(data))
        }
        return players
    }
}