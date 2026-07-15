package backStage

import android.util.Log
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class loginParseID(private val dsm: DataStoreManager) {
    suspend fun changeUUID(data: ByteBuffer) {
        val newID = StandardCharsets.UTF_8.decode(data).toString()
        dsm.saveIdMe(newID)
        Log.d("Logindump: ", newID)
    }
}
