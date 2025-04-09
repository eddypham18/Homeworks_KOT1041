package thaiph.ph48495.demo.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import thaiph.ph48495.demo.models.Food

@Composable
fun DialogEdit(isShow: Boolean, onDismiss: () -> Unit, onConfirm: (food:Food) -> Unit, food: Food) {
    var name by remember { mutableStateOf(food?.name ?: "") }
    var price by remember { mutableStateOf(food?.price?:"") }


    if (isShow) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(20.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Are you sure you want to edit this item?")

                    TextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = price,
                        onValueChange = {
                            price = it
                        },
                        label = { Text("Price") },
                        modifier = Modifier.fillMaxWidth()
                    )


                    Button(
                        onClick = { onConfirm(Food(food.id, name, food.avatar, price)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Confirm")
                    }
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}