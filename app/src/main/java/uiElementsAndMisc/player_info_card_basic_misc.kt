package uiElementsAndMisc

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.weaponizerzstudio.fieryescalation_gpsrts.R

@Preview
@Composable
fun DsiplayItems() {
    Column {
        Text(text = "Click to go to greeter.")

        LazyColumn(content = {
            items(getCategoryList()) { item ->
                Show_data_player_info_card(
                    img = item.img,
                    title = item.title,
                    sub_title = item.sub_title
                )
            }
        })
    }

}
@Composable
fun Show_data_player_info_card(img: Int, title: String, sub_title: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(color = Color.Black)

        ) {
            Image(painter = painterResource(
                id = img),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
                    .weight(.2f),
                contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
            )

            ItemDesc(title, sub_title, Modifier.weight(.8f))
        }
    }
}

@Composable
private fun RowScope.ItemDesc(
    title: String,
    sub_title: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = sub_title,
            fontWeight = FontWeight.ExtraLight,
            color = Color.Magenta
        )
    }
}
data class Category(val img: Int, val title: String, val sub_title: String)

fun getCategoryList(): MutableList<Category> {
    val list = mutableListOf<Category>()
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb", "a Tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb2", "a nother tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb3", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb4", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb", "a Tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb2", "a nother tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb3", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb4", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb", "a Tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb2", "a nother tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb3", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb4", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb", "a Tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb2", "a nother tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb3", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb4", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb", "a Tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb2", "a nother tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb3", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb4", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb", "a Tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb2", "a nother tank"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb3", "a person"))
    list.add(Category(R.drawable.bathtub_icon, "Weponized bthtb4", "a person"))

    return list
}







