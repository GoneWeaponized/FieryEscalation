package netTools

import android.util.Log
import backStage.byteParser
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import netTools.extras.ByteCommands
import java.nio.ByteBuffer
import kotlin.time.Duration.Companion.milliseconds

class FieryNetwork {

    private lateinit var socket: Socket
    private lateinit var reader: ByteReadChannel
    private lateinit var writer: ByteWriteChannel

    val sManager = SelectorManager(Dispatchers.IO)
    private val _isConnectedStatus = MutableStateFlow(false)
    val isConnectedStatus: StateFlow<Boolean> = _isConnectedStatus.asStateFlow()
    val isConnected: Boolean
        get() = ::socket.isInitialized && !socket.isClosed

    suspend fun voidConnect() = withContext(Dispatchers.IO) {
        if (isConnected) return@withContext

        socket = aSocket(sManager)
            .tcp()
            .connect(currentUrl, currentPort)
        reader = socket.openReadChannel()
        writer = socket.openWriteChannel(autoFlush = true)
        _isConnectedStatus.value = true
    }
    suspend fun loginSend(username: String, lat: Double, lng: Double, command: ByteCommands) {
        if (!isConnected) throw IllegalStateException("Network is not connected!")

        val nbytes = username.toByteArray(Charsets.UTF_8)
        val nLength = nbytes.size
        val buffer = ByteBuffer.allocate(2 + 1 + 2 + nLength + 16)
        buffer.putShort(buffer.capacity().toShort())
        buffer.put(command.ordinal.toByte())
        buffer.putShort(nLength.toShort())
        buffer.put(nbytes)
        buffer.putDouble(lat)
        buffer.putDouble(lng)
        buffer.flip()
        writer.writeFully(buffer)
    }

    suspend fun sendWriter(
        command: ByteCommands,
        lat: Double,
        long: Double,
        uuid: String
    ): Unit = withContext(Dispatchers.IO) {
        if (!isConnected) throw IllegalStateException("Cannot send packet, network is not connected!")

        val idBytes = uuid.toByteArray(Charsets.UTF_8)
        val buffer = ByteBuffer.allocate(2 + 1 + 8 + 8 + 4 + idBytes.size)
        buffer.putShort(buffer.capacity().toShort())
        buffer.put(command.ordinal.toByte())
        buffer.putDouble(lat)
        buffer.putDouble(long)
        buffer.putInt(idBytes.size)
        buffer.put(idBytes)
        buffer.flip()
        writer.writeFully(buffer)
    }
    suspend fun readerStart() = withContext(Dispatchers.IO) {
        if (!isConnectedStatus.value) throw IllegalStateException("Cannot read packet!")

        val buffer = ByteBuffer.allocate(65536) // for now, defaulting to 64kb


        while(true){
            val incomingBuffer = reader.readAvailable(buffer)
            if (incomingBuffer == 0) {delay(10.milliseconds)}
            if (incomingBuffer == -1) break
            buffer.flip()
            while (buffer.remaining() >= 2) {
                // move to pos 2
                buffer.mark() //mark to remember where we are
                val length = buffer.getShort().toInt() and 0xFFFF //get short form header and then make it Integer (unsigned)
                if (buffer.remaining() >= length - 2) {
                    val packet = ByteArray(length - 2)
                    buffer.get(packet)
                    byteParser(packet)
                    Log.d("Dump of data", packet.contentToString())
                } else {
                    buffer.reset()
                    break
                }
            }
            buffer.compact() // read the other headers in the packet
            // Allows u to parse many packets - I just solved a headache only to get another lmao
        }
    }

    suspend fun disconnect() = withContext(Dispatchers.IO) {
        if (::socket.isInitialized) {
            socket.close()
        }
        sManager.close()
    }
}
