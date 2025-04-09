package thaiph.ph48495.demo

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import thaiph.ph48495.demo.models.Food
import thaiph.ph48495.demo.screens.Details
import thaiph.ph48495.demo.screens.ListProduct
import thaiph.ph48495.demo.screens.Login
import thaiph.ph48495.demo.ui.theme.DemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            DemoTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MyApp(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "listProduct") {
                composable("login") {
                    Login(navController)
                }
                composable("listProduct") {
                    ListProduct(navController)
                }
                composable("details") {backStackEntry ->
                    val food = navController.previousBackStackEntry?.savedStateHandle?.get<Food>("food")
                    food?.let {
                        Details(food)
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
            contentDescription = "Login",
            alignment = Alignment.Center,
        )
        Text(
            text = "Login",
            modifier = modifier
                .padding(50.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            value = "$username",
            onValueChange = { username = it },
            placeholder = { Text("Username") }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            value = "$password",
            onValueChange = { password = it },
            placeholder = { Text("Password") }
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (username == "admin" && password == "admin") {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        showDialog = true
                        title = "Login successful"
                    } else {
                        Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        showDialog = true
                        title = "Invalid username or password"
                    }
                },
                modifier = Modifier
                    .padding(start = 10.dp, top = 30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_icon),
                    contentDescription = "Login",
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text("Login")
            }
            Button(
                onClick = {}, modifier = Modifier
                    .padding(start = 10.dp, top = 30.dp)
            ) {
                Text("Sign Up")
            }
        }
    }

    // Hiển thị Dialog nếu đăng nhập thành công
    if (showDialog) {
        DialogLogin(isShow = showDialog, onDismiss = { showDialog = false }, title = "$title")
    }
}


@Composable
fun DialogLogin(isShow: Boolean, onDismiss: () -> Unit, title: String) {
    if (isShow) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(20.dp)
                    .height(200.dp),
                shape = RoundedCornerShape(10.dp),
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$title")
                    Button(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun GreetingPreview() {
    DemoTheme {
        MyApp()
    }
}