package thai.phph48495.dethithu02.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import thai.phph48495.dethithu02.R

@Composable
fun PH48495LoginScreen(navController: NavController) {

    var input by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_fpt),
            contentDescription = "Logo FPT",
            modifier = Modifier.size(150.dp).clip(shape = CircleShape).border(5.dp, Color.Green, CircleShape)
        )
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text(text = "Nhap ma sv") }
        )
        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = {
                if(input.isNotEmpty()){
                    if(input == "PH48495"){
                        navController.navigate("home")
                    } else {
                        Toast.makeText(context, "Sai ma sv", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Nhap ma sv", Toast.LENGTH_SHORT).show()
                }

            }
        ) {
            Text(text = "Login")
        }
    }
}