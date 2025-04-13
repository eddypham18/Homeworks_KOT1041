package thaiph.ph48495.demo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import thaiph.ph48495.demo.models.Food

@Composable
fun Details(food: Food){
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = food.name)
        Spacer(modifier = Modifier.height(16.dp))
        AsyncImage(
            model = food.avatar,
            contentDescription = "",
            modifier = Modifier
                .padding(16.dp)
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = food.price)

    }
}