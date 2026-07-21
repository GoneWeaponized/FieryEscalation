package uiElementsAndMisc.bottomBar

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import kotlinx.coroutines.launch

@Composable
fun BottomBar(
    onInventoryClick: () -> Unit,
    isInventoryOpen: Boolean = false
) {
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier) {
        Row(modifier = Modifier.padding(bottom = 40.dp, start = 10.dp)) {
            Box(
                modifier = Modifier

                    .size(75.dp, 75.dp)
                    .background(Color.DarkGray.copy(0.3f), shape = RoundedCornerShape(0.dp))
                    .clickable(
                        onClick = {
                            scope.launch {

                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(id = R.drawable.baseline_add_circle_24), tint = Color.White, contentDescription = "BuildHere")
            }
            // Inventory
            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(75.dp, 75.dp)
                    .background(
                        if (isInventoryOpen) Color.Black.copy(0.4f) else Color.DarkGray.copy(0.3f),
                        shape = RoundedCornerShape(0.dp)
                    )
                    .clickable(onClick = onInventoryClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bag_personal),
                    tint = if (isInventoryOpen) Color.Yellow else Color.White,
                    contentDescription = "Inventory"
                )
            }
        }
    }
}
