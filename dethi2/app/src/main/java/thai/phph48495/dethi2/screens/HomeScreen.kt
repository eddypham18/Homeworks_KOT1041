package thai.phph48495.dethi2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import thai.phph48495.dethi2.dialogs.DeleteDialog
import thai.phph48495.dethi2.dialogs.DetailDialog
import thai.phph48495.dethi2.models.Car
import thai.phph48495.dethi2.viewmodels.CarViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val carViewModel : CarViewModel = viewModel()
    val carList = carViewModel.carList.value
    var selectedCar by remember { mutableStateOf<Car?>(null) }
    var selectedDeleteCar by remember { mutableStateOf<Car?>(null) }

    LaunchedEffect(Unit) {
        carViewModel.getAllCars()
    }

    if(selectedCar != null){
        DetailDialog(
            car = selectedCar!!,
            onDismiss = { selectedCar = null }
        )
    }

    if(selectedDeleteCar != null){
        DeleteDialog(
            onDismiss = { selectedDeleteCar = null },
            onConfirm = {
                carViewModel.deleteCar(selectedDeleteCar!!.id)
                selectedDeleteCar = null
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {

        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate("addCar")
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Magenta
            )) {
            Text(text = "Thêm xe mới", color = Color.White)
        }

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = "Danh sách xe",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(carList.size){ index ->
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable{
                            selectedCar = carList[index]
                        },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = carList[index].image,
                            contentDescription = "Car Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = carList[index].name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.size(10.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate("editCar/${carList[index].id}") },
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF6200EE),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Sửa", color = Color.White)
                        }

                        Button(
                            onClick = {
                                selectedDeleteCar = carList[index]
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Xóa", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}