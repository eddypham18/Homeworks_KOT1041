package thai.phph48495.test1vid.dialogs

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import thai.phph48495.test1vid.models.Cat

@Composable
fun DetailDialog(cat : Cat, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                AsyncImage(
                    model = cat.image,
                    contentDescription = "",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ten meo: ${cat.name}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mo ta: ${cat.tags.joinToString(" - ")}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    onDismiss()
                }) {
                    Text(
                        text = "Dong"
                    )
                }
            }
        }
    }
}
