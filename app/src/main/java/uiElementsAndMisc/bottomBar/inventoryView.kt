package uiElementsAndMisc.bottomBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import fieryTypes.ResourceTypes
import fieryTypes.Resources

@Composable
fun InventoryViewUI(
    modifier: Modifier = Modifier,
    data: List<Resources>? = null,
    onItemClick: (Resources) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.DarkGray.copy(alpha = 0.8f), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(12.dp)
    ) {
        if (!data.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(data) { resource ->
                    ResourceRow(resource, onClick = { onItemClick(resource) })
                }
            }
        } else {
            // If null or empty, show a message or just an empty space
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Inventory Empty", color = Color.Gray)
            }
        }
    }
}

@Composable
fun ResourceRow(resource: Resources, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = getResourceIcon(resource.type)),
                contentDescription = resource.type.name,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            Text(
                text = resource.type.name,
                color = Color.White,
                modifier = Modifier.padding(start = 12.dp),
                fontSize = 20.sp
            )
        }
        Text(
            text = resource.count?.toString() ?: "0",
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

fun getResourceIcon(type: ResourceTypes): Int {
    return when (type) {
        ResourceTypes.MANPOWER -> R.drawable.human
        ResourceTypes.MONEY -> R.drawable.cash
    }
}
