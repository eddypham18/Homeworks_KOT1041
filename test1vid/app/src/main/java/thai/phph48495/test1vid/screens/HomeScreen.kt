package thai.phph48495.test1vid.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
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
import thai.phph48495.test1vid.dialogs.DetailDialog
import thai.phph48495.test1vid.models.Cat
import thai.phph48495.test1vid.viewmodels.CatViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    val catViewModel : CatViewModel = viewModel()
    var listCat = catViewModel.listCat.value

    var selectedCat by remember { mutableStateOf<Cat?>(null) }

    if(selectedCat != null){
        DetailDialog(
            cat = selectedCat!!,
            onDismiss = { selectedCat = null }
        )
    }

    LaunchedEffect(Unit) {
        catViewModel.getAllCats()
    }

    Column {
        Button(
            onClick = {
                navController.navigate("an")
            }
        ) {
            Text("Chuyen den Screen 2")
        }
        LazyColumn {
            items(listCat.size){index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(
                            onClick = {
                                selectedCat = listCat[index]
                            }
                        )
                ) {
                    Row(
                        modifier = Modifier.fillParentMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Ten meo: ${listCat[index].name}"
                        )

                        AsyncImage(
                            model = listCat[index].image,
                            contentDescription = "",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }
        }
    }
}