package thaiph.ph48495.demo.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import thaiph.ph48495.demo.R
import thaiph.ph48495.demo.dialogs.DialogDelete
import thaiph.ph48495.demo.dialogs.DialogEdit
import thaiph.ph48495.demo.models.Food
import thaiph.ph48495.demo.service.ViewModelApp
import kotlin.math.log

val TAG = "zzzzzzzzzzzzzzzzz"

@Composable
fun ListProduct(navController: NavController, viewModel: ViewModelApp = viewModel()) {
    val context = LocalContext.current
    val listFood by viewModel.listFood
    var isShowDialogDelete by remember { mutableStateOf(false) }
    var isShowDialogEdit by remember { mutableStateOf(false) }
    var isSelected by remember { mutableStateOf<Food?>(null) }


    LaunchedEffect(key1 = Unit) {
        viewModel.getAllFoods()
    }

    DialogDelete(
        isShowDialogDelete,
        onConfirm = {
            viewModel.deleteFood(isSelected!!.id.toString())
            isShowDialogDelete = false
        },
        onDismiss = {isShowDialogDelete = false})

    isSelected?.let {
        DialogEdit(
        isShowDialogEdit,
        onConfirm = { newFood ->
            viewModel.editFood(it.id.toString(), newFood)
            isShowDialogEdit = false
        },
        onDismiss = {isShowDialogEdit = false
                    isSelected = null},
        food = it
    )
    }

    LazyColumn {
        listFood?.let {
            items(it.size) { index ->
                Card(onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("food", listFood!![index])
                    navController.navigate("details")
                },
                    modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp)) {
                    Row(modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        AsyncImage(
                            model = listFood!![index].avatar,
                            contentDescription = "",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp),
                            placeholder = painterResource(id = R.drawable.ic_launcher_background),
                            error = painterResource(id = R.drawable.ic_launcher_foreground)
                        )

                        Column (modifier = Modifier.padding(start = 10.dp)){
                            Text(text = listFood!![index].name)
                            Text(text = listFood!![index].price)
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    isSelected = listFood!![index]
                                    isShowDialogEdit = true
                                }
                        )
                        Image(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    isSelected = listFood!![index]
                                    isShowDialogDelete = true
                                },

                        )
                    }
                }
            }
        }
    }
}