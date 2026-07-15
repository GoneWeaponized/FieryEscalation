package uiElementsAndMisc

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import backStage.DataStoreManager
import backStage.DataStoreManager.Companion.UUID_KEY
import kotlinx.coroutines.launch

@Composable
fun UuidLoginScreen(onDismiss: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val savedUuid by dataStoreManager.getMyId.collectAsState(initial = "$UUID_KEY")
    AlertDialog(
        onDismissRequest = {},
        title = { Text("UUID Login.")},
        text = {
            Column {
                Text(text = "Please copy your Existing UUID below. Use it to log back in. \n Modifying the UUID will change your current User.")
                OutlinedTextField(
                    value = savedUuid,
                    label = { Text(text = "Enter UUID here") },
                    onValueChange = { newValue ->
                        scope.launch { dataStoreManager.saveIdMe(newValue) }
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            OutlinedButton(onClick = {
                focusManager.clearFocus()
            }) { Text(text = "Save ID") }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismiss() }) { Text(text = "Cancel") }
        }
    )

}