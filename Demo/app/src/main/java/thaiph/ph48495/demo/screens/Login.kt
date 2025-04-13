package thaiph.ph48495.demo.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import thaiph.ph48495.demo.R

@Composable
fun Login(navController: NavController){
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(16.dp),
            painter = painterResource(id = R.drawable.login_icon),
            contentDescription = "Logo"
        )
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = {
                Text(text = "Username")
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = passWord,
            onValueChange = { passWord = it },
            label = {
                Text(text = "Password")
            },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            checkLogin(
                userName,
                passWord,
                context,
                navController
            )
        }) { Text(text = "Login") }
    }
}

fun checkLogin(user: String, pass: String, context: Context, navController: NavController) {
    if (user.isNotBlank() && pass.isNotBlank()) {
        if (user == "1" && pass == "1") {
            navController.navigate("listProduct")
        } else {
            Toast.makeText(context, "Username or password is not correct", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Please enter username and password!", Toast.LENGTH_LONG).show()
    }
}