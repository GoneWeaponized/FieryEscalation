package uiElementsAndMisc

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import backStage.viewModels.NetworkViewModel
import kotlinx.coroutines.launch
import netTools.extras.ByteCommands

@Preview
@Composable
fun UnitTEst() {
    val scope = rememberCoroutineScope()
    val command = "GET_PLAYERS|ed1a9728-4328-45d8-8455-d2897abb9656|45.232|23.3221"
    var resp: String? by remember { mutableStateOf(null) }
    Column {
        Text(text = "HELLO $resp")
        Button(
            onClick = {
                scope.launch {
                    try {


                        NetworkViewModel().send(cmd = ByteCommands.GET_PLAYERS, lt = 34.34332, ln = 23.8867565, uu = "b1968097-e099-4195-8b9d-d586d82d816a")

                    } catch (e: Exception) {
                        Log.e("UnitTEst", "Network error", e)
                        resp = "Error: ${e.message}"
                    }
                }
            }
        ) { Text(text = "Clik")}

    }
}