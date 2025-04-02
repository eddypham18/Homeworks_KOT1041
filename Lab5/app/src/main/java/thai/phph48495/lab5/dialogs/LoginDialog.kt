package thai.phph48495.lab5.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun LoginDialog(isShow : Boolean, onDismiss : () -> Unit, message : String){
    if (isShow) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Notification") },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }
}