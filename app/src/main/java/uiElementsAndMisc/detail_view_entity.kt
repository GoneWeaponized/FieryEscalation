package uiElementsAndMisc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weaponizerzstudio.fieryescalation_gpsrts.R
import fieryTypes.TypeEntity

@Preview
@Composable
fun EntityDetailView(entityType: TypeEntity = TypeEntity.PLAYER, name: String? = "0", modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedCard(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color.DarkGray.copy(0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                when (entityType) {
                    TypeEntity.PLAYER -> Icon(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        tint = Color.White,
                        contentDescription = "Player Icon"
                    )
                    // add more later
                    else -> Icon(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        tint = Color.White,
                        contentDescription = "Player Icon"
                    )
                }
                Text(
                    text = name ?: "Default (report a bug)",
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.White
                )
            }
        }
    }
}
