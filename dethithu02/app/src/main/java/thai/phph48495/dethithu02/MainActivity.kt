package thai.phph48495.dethithu02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import thai.phph48495.dethithu02.screens.AddDogScreen
import thai.phph48495.dethithu02.screens.HomeScreen
import thai.phph48495.dethithu02.screens.PH48495LoginScreen
import thai.phph48495.dethithu02.screens.UpdateDogScreen
import thai.phph48495.dethithu02.ui.theme.Dethithu02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dethithu02Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        composable("login") {
                            PH48495LoginScreen(navController)
                        }

                        composable("home") {
                            HomeScreen(navController)
                        }

                        composable("addDog") {
                            AddDogScreen(navController)
                        }

                        composable("editDog/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")
                            if (id != null) {
                                UpdateDogScreen(id,navController)
                            } else {
                                Text(text = "ID not found")
                            }
                        }
                    }
                }
            }
        }
    }
}

