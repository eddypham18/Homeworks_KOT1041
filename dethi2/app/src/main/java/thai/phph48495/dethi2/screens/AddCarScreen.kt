package thai.phph48495.dethi2.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import thai.phph48495.dethi2.models.Car
import thai.phph48495.dethi2.viewmodels.CarViewModel
import kotlin.random.Random

@Composable
fun AddCarScreen(navController: NavController) {

    val localContext = LocalContext.current
    val carViewModel : CarViewModel = viewModel()
    var name by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            text = "Thêm xe mới",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Tên xe") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = image,
            onValueChange = { image = it },
            label = { Text("Ảnh xe") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Mô tả") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Button(
            onClick = {
                addCar(name, image, description, carViewModel, localContext, navController)
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Thêm xe", color = Color.White)
        }
    }
}

fun addCar(name : String, image : String, description : String, viewModel: CarViewModel, context: Context, navController: NavController) {
    //Validate data
    if(name.isEmpty() || image.isEmpty() || description.isEmpty()) {
        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        return
    }

    if(!image.startsWith("https")) {
        Toast.makeText(context, "Vui lòng nhập đúng định dạng ảnh", Toast.LENGTH_SHORT).show()
        return
    }

    //Add car
    val id = Random.nextInt(1000, 9999).toString()
    val car = Car(id,name, image, description)
    viewModel.addCar(car,navController)
}