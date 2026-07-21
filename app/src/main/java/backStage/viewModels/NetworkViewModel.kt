package backStage.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import netTools.FieryNetwork
import netTools.extras.ByteCommands

class NetworkViewModel : ViewModel() {

    private val fieryNetwork = FieryNetwork()
    val connectionState = fieryNetwork.isConnectedStatus
    val isConnected: Boolean
        get() = fieryNetwork.isConnected

    suspend fun connect() {
        try {
            fieryNetwork.voidConnect()
        } catch (e: Exception) {
            Log.e("Error netowrk", e.toString())
        }
    }
    suspend fun readStart() {
        fieryNetwork.readerStart()
    }
    suspend fun loginEvn(cmd: ByteCommands, uu: String) {
        try {
            fieryNetwork.loginEvent(cmd, uu)
        } catch (e: Exception) {
            Log.e("NetworkViewModel", "loginEvn failed: ${e.message}")
        }
    }
    suspend fun loginWrite(name: String, lat: Double, long: Double, command: ByteCommands) {
        try {
            fieryNetwork.loginSend(name, lat, long, command)
        } catch (e: Exception) {
            Log.e("NetworkViewModel", "loginWrite failed: ${e.message}")
        }
    }
    fun send(cmd: ByteCommands, lt: Double, ln: Double, uu: String) {
        viewModelScope.launch {
            try {
                fieryNetwork.getPlayers(cmd, lt, ln, uu)
            } catch (e: Exception) {
                Log.e("Err VM", "Failed to send: ${e.message}")
            }
        }
    }

    fun login(username: String, myLat: Double?, myLong: Double?) {

    }
}