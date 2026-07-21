package backStage.dataParsing

import android.util.Log
import fieryTypes.parseOperations.InvParse
import backStage.DataStoreManager
import backStage.loginParseID
import java.nio.ByteBuffer
import mapItemLoading.entityParser
import com.weaponizerzstudio.fieryescalation_gpsrts.Defs

suspend fun byteParser(data: ByteArray) {
       val invParse = InvParse.instance
       val dsm = DataStoreManager.instance
       val defs = Defs()
       val bufData = ByteBuffer.wrap(data)
       val type = bufData.get().toInt() and 0xFF

       when(type) {
              defs.login -> loginParseID(dsm = dsm).changeUUID(bufData)
              defs.getPlayers -> entityParser.parseAllPlayers(bufData)
              defs.getStructures -> TODO()
              defs.playerDataMine -> invParse.parseInv(bufData)
              defs.error -> Log.d("ERROR FROM SERVER", "TYPE 6 RECEIVED.")
              else -> return
       }
}
