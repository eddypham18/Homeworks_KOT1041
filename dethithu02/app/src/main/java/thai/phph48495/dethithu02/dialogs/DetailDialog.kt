package thai.phph48495.dethithu02.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import thai.phph48495.dethithu02.models.Dog

@Composable
fun DetailDialog(dog: Dog, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = dog.image,
                    contentDescription = "",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "Ten: ${dog.name}", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "Mo ta: ${dog.description}", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Dong", color = Color.White)
                }
            }
        }
    }
}