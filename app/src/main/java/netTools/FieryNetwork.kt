package netTools

import android.util.Log
import backStage.dataParsing.byteParser
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import netTools.extras.ByteCommands
import java.nio.ByteBuffer
import kotlin.time.Duration.Companion.milliseconds

class FieryNetwork {

    private lateinit var socket: Socket
    private lateinit var reader: ByteReadChannel
    private lateinit var writer: ByteWriteChannel

    private val networkMutex = Mutex()

    val sManager = SelectorManager(Dispatchers.IO)
    private val _isConnectedStatus = MutableStateFlow(false)
    val isConnectedStatus: StateFlow<Boolean> = _isConnectedStatus.asStateFlow()
    val isConnected: Boolean
        get() = ::socket.isInitialized && !socket.isClosed && _isConnectedStatus.value

    suspend fun voidConnect() = networkMutex.withLock {
        if (isConnected) return@withLock

        withContext(Dispatchers.IO) {
            try {
                socket = aSocket(sManager)
                    .tcp()
                    .connect(currentUrl, currentPort)
                reader = socket.openReadChannel()
                writer = socket.openWriteChannel(autoFlush = true)
                _isConnectedStatus.value = true
            } catch (e: Exception) {
                _isConnectedStatus.value = false
                throw e
            }
        }
    }

    private suspend fun writeSafe(buffer: ByteBuffer) = networkMutex.withLock {
        try {
            if (!isConnected) {
                Log.e("FieryNetwork", "writeSafe failed: Network is not connected!")
                throw IllegalStateException("Network is not connected!")
            }
            if (!this@FieryNetwork::writer.isInitialized) {
                Log.e("FieryNetwork", "writeSafe failed: writer is not initialized!")
                throw IllegalStateException("Writer not initialized!")
            }
            writer.writeFully(buffer)
        } catch (e: Exception) {
            Log.e("FieryNetwork", "writeSafe Exception: ${e.message}", e)
            _isConnectedStatus.value = false
            if (::socket.isInitialized && !socket.isClosed) {
                socket.close()
            }
            throw e
        }
    }

    suspend fun loginSend(username: String, lat: Double, lng: Double, command: ByteCommands) {
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
        writeSafe(buffer)
    }

    suspend fun getPlayers(
        command: ByteCommands,
        lat: Double,
        long: Double,
        uuid: String
    ): Unit = withContext(Dispatchers.IO) {
        val idBytes = uuid.toByteArray(Charsets.UTF_8)
        val buffer = ByteBuffer.allocate(2 + 1 + 8 + 8 + 4 + idBytes.size)
        buffer.putShort(buffer.capacity().toShort())
        buffer.put(command.ordinal.toByte())
        buffer.putDouble(lat)
        buffer.putDouble(long)
        buffer.putInt(idBytes.size)
        buffer.put(idBytes)
        buffer.flip()
        writeSafe(buffer)
    }

    suspend fun loginEvent(command: ByteCommands, uuid: String): Unit = withContext(Dispatchers.IO) {
        val idBytes = uuid.toByteArray(Charsets.UTF_8)
        val buffer = ByteBuffer.allocate(idBytes.size + 5)
        buffer.putShort(buffer.capacity().toShort())
        buffer.put(command.ordinal.toByte())
        buffer.putShort(idBytes.size.toShort())
        buffer.put(idBytes)
        buffer.flip()
        writeSafe(buffer)
    }

    suspend fun readerStart() = withContext(Dispatchers.IO) {
        if (!isConnectedStatus.value) {
            Log.e("FieryNetwork", "readerStart failed: Not connected")
            throw IllegalStateException("Cannot read packet!")
        }
        if (!this@FieryNetwork::reader.isInitialized) {
            Log.e("FieryNetwork", "readerStart failed: reader is not initialized!")
            throw IllegalStateException("Reader not initialized!")
        }

        Log.d("FieryNetwork", "Reader started")
        val buffer = ByteBuffer.allocate(65536) // for now, defaulting to 64kb

        try {
            while (true) {
                val incomingBuffer = reader.readAvailable(buffer)
                if (incomingBuffer == 0) {
                    delay(10.milliseconds)
                    continue
                }
                if (incomingBuffer == -1) {
                    Log.d("FieryNetwork", "Reader reached EOF (-1)")
                    break
                }
                
                buffer.flip()
                while (buffer.remaining() >= 2) {
                    buffer.mark()
                    val length = buffer.getShort().toInt() and 0xFFFF
                    
                    if (buffer.remaining() >= length - 2) {
                        val packet = ByteArray(length - 2)
                        buffer.get(packet)
                        try {
                            byteParser(packet)
                        } catch (e: Exception) {
                            Log.e("FieryNetwork", "Error parsing packet of type ${packet.getOrNull(0)}: ${e.message}")
                        }
                    } else {
                        buffer.reset()
                        break
                    }
                }
                buffer.compact()
            }
        } catch (e: Exception) {
            Log.e("FieryNetwork", "Reader loop Exception: ${e.message}", e)
        } finally {
            Log.d("FieryNetwork", "Reader stopping, closing socket")
            _isConnectedStatus.value = false
            if (::socket.isInitialized && !socket.isClosed) {
                socket.close()
            }
        }
    }

    suspend fun disconnect() = withContext(Dispatchers.IO) {
        if (::socket.isInitialized) {
            socket.close()
        }
        sManager.close()
    }
}
