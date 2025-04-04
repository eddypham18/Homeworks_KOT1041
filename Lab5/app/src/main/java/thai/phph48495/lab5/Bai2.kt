package thai.phph48495.lab5

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Bai2(nav: NavController){

    var isCheck by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(isCheck){
            Image(
                painter = painterResource(id = R.drawable.bulb_off),
                contentDescription = "Bulb off",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.bulb_on),
                contentDescription = "Bulb on",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }

        Spacer(modifier = Modifier.fillMaxWidth().background(Color.Black).height(16.dp))

        Switch(
            checked = isCheck,
            onCheckedChange = {isCheck=it},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Yellow,
                checkedTrackColor = Color.Gray,
                uncheckedThumbColor = Color.Yellow,
                uncheckedTrackColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.fillMaxWidth().background(Color.Black).height(16.dp))
        Button(
            onClick = {
                nav.navigate("Bai3")
            }
        ) {
            Text(text = "Sang bài 3")
        }
    }
}