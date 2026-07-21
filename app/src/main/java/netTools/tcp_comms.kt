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
    // Was used in text protocols. Might use again.
}