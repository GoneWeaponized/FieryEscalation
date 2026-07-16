package backStage

import java.nio.ByteBuffer
import mapItemLoading.entityParser

suspend fun byteParser(data: ByteArray) {
       val dsm = DataStoreManager.instance
       val bufData = ByteBuffer.wrap(data)
       val type = bufData.get().toInt() and 0xFF

       when(type) {
              0 -> loginParseID(dsm = dsm).changeUUID(bufData)
              1 -> entityParser.parseAllPlayers(bufData)
              else -> return
       }
}
