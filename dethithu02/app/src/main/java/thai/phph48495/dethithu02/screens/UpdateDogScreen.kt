package thai.phph48495.dethithu02.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import thai.phph48495.dethithu02.models.Dog
import thai.phph48495.dethithu02.viewmodels.DogViewModel
import kotlin.random.Random

@Composable
fun UpdateDogScreen(id: String,navController: NavController) {

    val dogViewModel: DogViewModel = viewModel()
    val dog = dogViewModel.dogList.value.find { it.id == id }


    val context = LocalContext.current
    var name by remember(dog) { mutableStateOf(dog?.name ?:"") }
    var image by remember(dog) { mutableStateOf(dog?.image ?:"") }
    var description by remember(dog) { mutableStateOf(dog?.description ?:"") }

    LaunchedEffect(Unit) {
        dogViewModel.getAllDogs()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Sua con cho",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { androidx.compose.material.Text(text = "Ten cho") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = image,
            onValueChange = { image = it },
            label = { androidx.compose.material.Text(text = "Anh") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { androidx.compose.material.Text(text = "Mo ta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(10.dp))
        Button(
            onClick = {
                if(name.isNotEmpty() && image.isNotEmpty() && description.isNotEmpty()){
                    val dog = Dog(id, name, image, description)

                    val res = dogViewModel.updateDog(id, dog)
                    if(res != null){
                        navController.popBackStack()
                    }
                } else {
                    Toast.makeText(context, "Vui long nhap day du thong tin", Toast.LENGTH_SHORT).show()
                    return@Button
                }
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Sua",
                color = Color.White
            )
        }
    }
}