package netTools

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

var currentUrl: String = "127.0.0.1"
var currentPort: Int = 5010
// tcp_comms.kt
object TcpClienter {
    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null
    private var lastUsedIp: String? = null

    suspend fun fetchMessage(cmd: String = "TheFault"): String {
        return withContext(Dispatchers.IO) {
            try {
                // If the global currentUrl has changed, force close the old socket
                if (socket != null &&( lastUsedIp != currentUrl || socket?.port != currentPort )) {
                    socket?.close()
                    socket = null
                }

                if (socket == null || socket!!.isClosed || !socket!!.isConnected) {
                    socket = Socket(currentUrl, currentPort)
                    lastUsedIp = currentUrl // Update our tracker
                    socket!!.tcpNoDelay = true
                    writer = PrintWriter(socket!!.getOutputStream(), true)
                    reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                }

                writer?.println(cmd)
                val response = reader?.readLine()
                response ?: "No response"
            } catch (e: Exception) {
                socket = null
                "Something happened: ${e.message}"
            }
        }
    }
    suspend fun loginToServer(username: String, lat: Double?, long: Double?): String {
        val command = "LOGIN|$username|$lat|$long"
        return fetchMessage(command) // Sends back the server response.
    }
}