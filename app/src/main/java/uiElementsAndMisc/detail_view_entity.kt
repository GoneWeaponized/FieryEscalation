package uiElementsAndMisc

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import fieryEntity.TypeEntity
@Preview
@Composable
fun EntityDetailView(entityType: TypeEntity = TypeEntity.PLAYER, entityId: String = "0") {
    Row(modifier = Modifier.fillMaxWidth() .height(300.dp),
        verticalAlignment = Alignment.Bottom) {
        Card {
            when (entityType) {
                TypeEntity.PLAYER -> Icon(painter = painterResource(id = R.drawable.baseline_person_24), tint = Color.White,
                    contentDescription = "Player Icon")
                // add more later
                else -> Icon(painter = painterResource(id = R.drawable.baseline_person_24), tint = Color.White,
                    contentDescription = "Player Icon")
            }
            Text(text = "$entityType")
        }
    }
}