package thai.phph48495.dethithu02.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.dethithu02.dialogs.DeleteDialog
import thai.phph48495.dethithu02.dialogs.DetailDialog
import thai.phph48495.dethithu02.models.Dog
import thai.phph48495.dethithu02.viewmodels.DogViewModel

@Composable
fun HomeScreen(navController: NavController) {

    val dogViewModel : DogViewModel = viewModel()
    val dogList = dogViewModel.dogList.value
    var selectedDog by remember { mutableStateOf<Dog?>(null)}
    var selectedDeleteDog by remember { mutableStateOf<Dog?>(null)}

    LaunchedEffect(Unit) {
        dogViewModel.getAllDogs()
    }

    if(selectedDog != null){
        DetailDialog(selectedDog!!, onDismiss = { selectedDog = null})
    }

    if(selectedDeleteDog != null){
        DeleteDialog(
            onDismiss = { selectedDeleteDog = null},
            onConfirm = {
                dogViewModel.deleteDog(selectedDeleteDog!!.id)
                selectedDeleteDog = null
            }

        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Button(
            onClick = { navController.navigate("addDog")},
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        ) {
            Text(
                text = "Them cho moi"
            )
        }
        Spacer(modifier = Modifier.size(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(dogList.size) { index ->
                Column {
                    Card(
                        modifier = Modifier.padding(10.dp).fillMaxWidth().clickable { selectedDog = dogList[index] }
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = dogList[index].image,
                                contentDescription = "",
                                modifier = Modifier.size(100.dp)
                            )
                            Text(text = "Ten: ${dogList[index].name}", style = MaterialTheme.typography.titleLarge)
                        }

                    }

                    Row(modifier = Modifier.fillParentMaxWidth().padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,) {
                        Button(
                            onClick = {
                                selectedDeleteDog = dogList[index]
                            }
                        ) {
                            Text(
                                text = "Xoa",
                                color = MaterialTheme.colorScheme.onError,
                            )
                        }

                        Button(
                            onClick = {
                                navController.navigate("editDog/${dogList[index].id}")
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "Sua",
                                color = MaterialTheme.colorScheme.onError,
                            )
                        }
                    }
                }
            }
        }
    }
}