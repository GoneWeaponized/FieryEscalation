package netTools

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import netTools.extras.ByteCommands
import java.nio.ByteBuffer

class FieryNetwork {

    private lateinit var socket: Socket
    private lateinit var reader: ByteReadChannel
    private lateinit var writer: ByteWriteChannel
    val sManager = SelectorManager(Dispatchers.IO)
    val rocketSocket = aSocket(sManager)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

     suspend fun voidConnect() = withContext(Dispatchers.IO) {

        socket = aSocket(sManager)
            .tcp()
            .connect(currentUrl, currentPort)

        reader = socket.openReadChannel()
        writer = socket.openWriteChannel(autoFlush = true)

        }
    fun startPrayer() {
        scope.launch {
            while (!socket.isClosed) {
                TODO()
            }
        }
    }
    suspend fun sendWriter(
        command: ByteCommands,
        lat: Double,
        long: Double,
        uuid: String
    ): Unit = withContext(Dispatchers.IO)
    {
        val launchpad = aSocket(sManager).tcp().connect(currentUrl, currentPort)

        launchpad.use { _ ->
            val writeLyric = launchpad.openWriteChannel(autoFlush = true)
            val idBytes = uuid.toByteArray(Charsets.UTF_8)
            val buffer = ByteBuffer.allocate(2 + 1 + 8 + 8 + 4 + idBytes.size)
            buffer.putShort(buffer.capacity().toShort())
            buffer.put(command.ordinal.toByte())
            buffer.putDouble(lat)
            buffer.putDouble(long)
            buffer.putInt(idBytes.size)
            buffer.put(idBytes)
            buffer.flip()

            writeLyric.writeFully(buffer)

        }
        //vibe coding because i'm vibing with it
    }
    suspend fun bufReader(command: ByteCommands) {
        val landing = rocketSocket.tcp().connect(currentUrl,currentPort)
        landing.openReadChannel()

    }
}